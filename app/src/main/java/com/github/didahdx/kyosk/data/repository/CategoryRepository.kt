package com.github.didahdx.kyosk.data.repository

import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import com.github.didahdx.kyosk.data.mapper.mapToProductEntity
import com.github.didahdx.kyosk.data.mapper.mapToProductItem
import com.github.didahdx.kyosk.data.remote.NetworkAvailability
import com.github.didahdx.kyosk.data.remote.api.ShopApiServices
import com.github.didahdx.kyosk.data.remote.dto.ProductItemDto
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Daniel Didah on 10/25/21.
 */
class CategoryRepository @Inject constructor(
    private val shopApiServices: ShopApiServices,
    private val productDao: ProductDao,
    private val networkAvailability: NetworkAvailability
) {
    private val compositeDisposable = CompositeDisposable()

    private fun getProducts(): Observable<List<ProductItemDto>> {
        return shopApiServices.getProducts()
    }

    private fun getAllProductsByCategory(categoryTitle: RecyclerViewItems.CategoryTitle): Observable<Resources<List<RecyclerViewItems>>> {
        return Observable.create { emitter ->
            emitter.onNext(Resources.Loading<List<RecyclerViewItems>>())

            val disposable = getProducts().subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .switchMap { productList ->
                    val product = productList.map { it.mapToProductEntity() }
                    productDao.insert(product)
                    return@switchMap productDao.getAllProductsByCategory(category = categoryTitle.code)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                }.map { productList ->
                    val list = ArrayList<RecyclerViewItems>()
//                    list.add(categoryTitle)
                    list.addAll(productList.map { it.mapToProductItem() })
                    list.toList()
                }.subscribe({
                    emitter.onNext(Resources.Success<List<RecyclerViewItems>>(it))
                }, { error ->
                    Timber.e(error)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(error.stackTraceToString()))
                })


            compositeDisposable.add(disposable)
        }
    }

    private fun getAllProductsByCategoryDb(categoryTitle: RecyclerViewItems.CategoryTitle): Observable<Resources<List<RecyclerViewItems>>> {
        return Observable.create { emitter ->
            emitter.onNext(Resources.Loading<List<RecyclerViewItems>>())

            val disposable = productDao.getAllProductsByCategory(category = categoryTitle.code)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { productList ->
                    val list = ArrayList<RecyclerViewItems>()
                    list.addAll(productList.map { it.mapToProductItem() })
                    list.toList()
                }
                .subscribe({
                    emitter.onNext(Resources.Success<List<RecyclerViewItems>>(it))
                }, { error ->
                    Timber.e(error)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(error.stackTraceToString()))
                })

            compositeDisposable.add(disposable)
        }
    }


    fun fetchAllProductsByCategory(categoryTitle: RecyclerViewItems.CategoryTitle): Observable<Resources<List<RecyclerViewItems>>> {
        return if (networkAvailability.isNetworkAvailable()) {
            getAllProductsByCategory(categoryTitle)
        } else {
            getAllProductsByCategoryDb(categoryTitle)
        }
    }

    fun onClear() {
        compositeDisposable.clear()
    }
}