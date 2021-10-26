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
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @AssistedInject constructor(
    private val shopRepository: ShopRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<HomeViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): HomeViewModel
    }

    var filterPublishSubject: PublishSubject<String> = PublishSubject.create()
    val filter = savedStateHandle.getLiveData("filter", "All")
    val allItems = MutableLiveData<Resources<List<RecyclerViewItems>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        getProductsData()
        setFilter(filter.value ?: "All")
    }

    private fun getProductsData() {
        val items = filterPublishSubject.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .switchMap { code ->
                return@switchMap shopRepository.fetchAll(code).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
            .subscribe({
                allItems.value = it
            }, {
                Timber.e(it)
                allItems.value = Resources.Error<List<RecyclerViewItems>>("Something went wrong")
            })

        compositeDisposable.add(items)
    }

    fun setFilter(code: String) {
        filter.value = code
        filterPublishSubject.onNext(code)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        shopRepository.clear()
    }

}