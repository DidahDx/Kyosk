package com.github.didahdx.kyosk.data.repository

import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.local.dao.ProductDao
import com.github.didahdx.kyosk.data.mapper.mapToProductItem
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Daniel Didah on 10/25/21.
 */
class ProductDetailRepository @Inject constructor(
    private val productDao: ProductDao
) {

    fun getProduct(id: Int): Observable<Resources<RecyclerViewItems.ProductItem>> {
        return Observable.create<Resources<RecyclerViewItems.ProductItem>>() { emitter ->
            emitter.onNext(Resources.Loading<RecyclerViewItems.ProductItem>())

            productDao.getAllProductsWithId(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ product ->
                    emitter.onNext(Resources.Success<RecyclerViewItems.ProductItem>(product.mapToProductItem()))
                }, {
                    Timber.e(it)
                    emitter.onNext(Resources.Error<RecyclerViewItems.ProductItem>("Something went wrong"))
                })
        }
    }
}