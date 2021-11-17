package com.github.didahdx.kyosk.data.repository

import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.local.dao.CategoryDao
import com.github.didahdx.kyosk.data.mapper.mapToCategoryEntity
import com.github.didahdx.kyosk.data.mapper.mapToCategoryTitle
import com.github.didahdx.kyosk.data.remote.NetworkAvailability
import com.github.didahdx.kyosk.data.remote.api.ShopApiServices
import com.github.didahdx.kyosk.data.remote.dto.CategoryListDto
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Daniel Didah on 10/25/21.
 */
class CategoriesRepository @Inject constructor(
    private val shopApiServices: ShopApiServices,
    private val categoryDao: CategoryDao,
    private val networkAvailability: NetworkAvailability
) {
    private val compositeDisposable = CompositeDisposable()

    private fun getCategories(): Observable<List<CategoryListDto>> {
        return shopApiServices.getCategories()
    }

    fun fetchAll(): Observable<Resources<List<RecyclerViewItems.CategoryTitle>>> {
        return if (networkAvailability.isNetworkAvailable()) {
            getAllCategories()
        } else {
            getAllCategoriesDb()
        }
    }

    private fun getAllCategories(): Observable<Resources<List<RecyclerViewItems.CategoryTitle>>> {
        return Observable.create<Resources<List<RecyclerViewItems.CategoryTitle>>>() { emitter ->
            emitter.onNext(Resources.Loading<List<RecyclerViewItems.CategoryTitle>>())

            compositeDisposable += getCategories().subscribeOn(Schedulers.io())
                .switchMap { categoryList ->
                    val categories =
                        categoryList.first().categories.map { category -> category.mapToCategoryEntity() }
                    categoryDao.insert(categories).subscribeOn(Schedulers.io()).subscribe()
                    return@switchMap categoryDao.getAllCategories().subscribeOn(Schedulers.io())
                }.map { category ->
                    category.map { it.mapToCategoryTitle() }
                }.subscribe({
                    Timber.e("$it")
                    emitter.onNext(Resources.Success<List<RecyclerViewItems.CategoryTitle>>(it))
                }, { error ->
                    Timber.e(error)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems.CategoryTitle>>(error.stackTraceToString()))
                })

        }
    }


    private fun getAllCategoriesDb(): Observable<Resources<List<RecyclerViewItems.CategoryTitle>>> {
        return Observable.create<Resources<List<RecyclerViewItems.CategoryTitle>>>() { emitter ->
            emitter.onNext(Resources.Loading<List<RecyclerViewItems.CategoryTitle>>())

            compositeDisposable += categoryDao.getAllCategories().subscribeOn(Schedulers.io())
                .map { category ->
                    category.map { it.mapToCategoryTitle() }
                }
                .subscribe({
                    Timber.e("$it")
                    emitter.onNext(Resources.Success<List<RecyclerViewItems.CategoryTitle>>(it))
                }, { error ->
                    Timber.e(error)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems.CategoryTitle>>(error.stackTraceToString()))
                })
        }
    }


    fun onClear() {
        compositeDisposable.clear()
    }
}