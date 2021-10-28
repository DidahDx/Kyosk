package com.github.didahdx.kyosk.data.repository

import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.local.dao.CategoryDao
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import com.github.didahdx.kyosk.data.mapper.*
import com.github.didahdx.kyosk.data.remote.NetworkAvailability
import com.github.didahdx.kyosk.data.remote.api.ShopApiServices
import com.github.didahdx.kyosk.data.remote.dto.CategoryListDto
import com.github.didahdx.kyosk.data.remote.dto.ProductItemDto
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Daniel Didah on 10/22/21.
 */
class ShopRepositoryImpl @Inject constructor(
    private val shopApiServices: ShopApiServices,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val networkAvailability: NetworkAvailability
) : ShopRepository {
    private val compositeDisposable = CompositeDisposable()

    override fun getCategories(): Observable<List<CategoryListDto>> {
        return shopApiServices.getCategories()
    }

    override fun getProducts(): Observable<List<ProductItemDto>> {
        return shopApiServices.getProducts()
    }

    override fun getAllProducts(code: String): Observable<Resources<List<RecyclerViewItems>>> {
        return Observable.create<Resources<List<RecyclerViewItems>>>() { emitter ->
            //display loading data
            emitter.onNext(Resources.Loading<List<RecyclerViewItems>>())

            //getting categories and products list (Network call)
            compositeDisposable += Observable.zip(getCategories().subscribeOn(Schedulers.io())
                .doOnError {
                    Timber.e(it)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(it.localizedMessage ?: it.stackTraceToString()))
                },
                getProducts().subscribeOn(Schedulers.io()).doOnError {
                    Timber.e(it)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(it.localizedMessage ?: it.stackTraceToString()))
                },
                { categoryList, productsItemsList ->
                    //get all categories and save to database
                    val categories = categoryList.first().categories.map { category ->
                        category.mapToCategoryEntity()
                    }
                    Timber.e("$categories")
                    categoryDao.insert(categories).subscribeOn(Schedulers.io()).subscribe()

                    //get all products and save to database
                    val products = productsItemsList.map { productItemDto ->
                        productItemDto.mapToProductEntity()
                    }
                    Timber.e("$products")
                    productDao.insert(products).subscribeOn(Schedulers.io()).subscribe()

                }).subscribeOn(Schedulers.io())
                .doOnError {
                    Timber.e(it)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(it.localizedMessage ?: it.stackTraceToString())) }
                .switchMap {
                    //get all categories and products from database
                    return@switchMap categoryDao.getAllCategories().subscribeOn(Schedulers.io())
                        .zipWith(categoryDao.getCategoriesAndProducts(code)
                            .subscribeOn(Schedulers.io()),
                            { categoriesList, categoryProducts ->
                                val productListItems = ArrayList<RecyclerViewItems>()

                                //All category List
                                productListItems.add(
                                    RecyclerViewItems.CategoryTitle(
                                        "Categories",
                                        "Categories"
                                    )
                                )
                                val items = categoriesList.mapToCategoryChipList(code)
                                productListItems.add(items)

                                //categories with its products list
                                categoryProducts.map { categoryProductsRelation ->
                                    productListItems.add(categoryProductsRelation.categoryEntity.mapToCategoryTitle())
                                    productListItems.add(categoryProductsRelation.mapToProductItemList())
                                }

                                //return the complete list
                                productListItems.toList()
                            })
                }.subscribe({ item ->
                    emitter.onNext(Resources.Success<List<RecyclerViewItems>>(item))
                    Timber.e("$item")
                }, { error ->
                    Timber.e(error)
                    emitter.onNext(
                        Resources.Error<List<RecyclerViewItems>>(
                            error.localizedMessage ?: error.stackTraceToString()
                        )
                    )
                })
        }
    }

    override fun fetchAll(code: String): Observable<Resources<List<RecyclerViewItems>>> {
        return if (networkAvailability.isNetworkAvailable()) {
            getAllProducts(code)
        } else {
            getAllProductsDb(code)
        }
    }

    override fun getAllProductsDb(code: String): Observable<Resources<List<RecyclerViewItems>>> {
        return Observable.create<Resources<List<RecyclerViewItems>>>() { emitter ->
            //display loading data
            emitter.onNext(Resources.Loading<List<RecyclerViewItems>>())

            compositeDisposable += categoryDao.getAllCategories().subscribeOn(Schedulers.io())
                .zipWith(categoryDao.getCategoriesAndProducts(code)
                    .subscribeOn(Schedulers.io()),
                    { categoriesList, categoryProducts ->
                        val productListItems = ArrayList<RecyclerViewItems>()

                        //All category List
                        productListItems.add(
                            RecyclerViewItems.CategoryTitle("Categories", "Categories")
                        )
                        val items = categoriesList.mapToCategoryChipList(code)
                        productListItems.add(items)

                        //categories with its products list
                        categoryProducts.map { categoryProductsRelation ->
                            productListItems.add(categoryProductsRelation.categoryEntity.mapToCategoryTitle())
                            productListItems.add(categoryProductsRelation.mapToProductItemList())
                        }

                        //return the complete list
                        productListItems.toList()
                    })
                .subscribeOn(Schedulers.io())
                .subscribe({ item ->
                    emitter.onNext(Resources.Success<List<RecyclerViewItems>>(item))
                    Timber.e("$item")
                }, {
                    Timber.e(it)
                    emitter.onNext(Resources.Error<List<RecyclerViewItems>>(
                        it.localizedMessage ?: it.stackTraceToString()))
                })
        }
    }


    override fun clear() {
        compositeDisposable.clear()
    }


}