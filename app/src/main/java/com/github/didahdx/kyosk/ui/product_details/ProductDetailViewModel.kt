package com.github.didahdx.kyosk.ui.product_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.repository.ProductDetailRepository
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class ProductDetailViewModel @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val items = MutableLiveData<Resources<RecyclerViewItems.ProductItem>>()
    fun getProduct(id: Int) {
        val dispose = productDetailRepository.getProduct(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ product ->
                items.value = product
            }, {
                Timber.e(it)
            })
        compositeDisposable.add(dispose)
    }


    override fun onCleared() {
        super.onCleared()
        productDetailRepository.clear()
        compositeDisposable.clear()
    }
}