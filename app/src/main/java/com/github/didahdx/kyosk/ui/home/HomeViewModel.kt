package com.github.didahdx.kyosk.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.repository.ShopRepository
import com.github.didahdx.kyosk.di.AssistedSavedStateViewModelFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HomeViewModel @AssistedInject constructor(
    private val shopRepository: ShopRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<HomeViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): HomeViewModel
    }

    companion object {
        const val FILTER_KEY = "filter"
        const val DEFAULT_FILTER_VALUE = "All"
    }

    var filterPublishSubject: PublishSubject<String> = PublishSubject.create()
    val filter = savedStateHandle.getLiveData(FILTER_KEY, DEFAULT_FILTER_VALUE)
    val allItems = MutableLiveData<Resources<List<RecyclerViewItems>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        getProductsData()
        setFilter(filter.value ?: DEFAULT_FILTER_VALUE)
    }

    private fun getProductsData() {
        compositeDisposable += filterPublishSubject.subscribeOn(Schedulers.io())
            .throttleLast(300, TimeUnit.MILLISECONDS)
            .switchMap { code ->
                Timber.e("Log last $code")
                return@switchMap shopRepository.fetchAll(code).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe({
                allItems.value = it
            }, {
                Timber.e(it)
                allItems.value = Resources.Error<List<RecyclerViewItems>>(
                    it.localizedMessage ?: it.stackTraceToString()
                )
            })

    }

    fun setFilter(code: String) {
        filter.value = code
        filterPublishSubject.onNext(code)
    }


    override fun onCleared() {
        super.onCleared()
        shopRepository.clear()
        compositeDisposable.clear()
    }

}