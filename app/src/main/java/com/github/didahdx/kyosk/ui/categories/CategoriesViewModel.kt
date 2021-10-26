package com.github.didahdx.kyosk.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.repository.CategoriesRepository
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    val categories = MutableLiveData<Resources<List<RecyclerViewItems.CategoryTitle>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchAllCategories()
    }

    private fun fetchAllCategories() {
        val dispose = categoriesRepository.fetchAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ item ->
                Timber.e("$item")
                categories.value = item
            }, {
                Timber.e(it)
                categories.value= Resources.Error<List<RecyclerViewItems.CategoryTitle>>("Something went wrong")
            })
        compositeDisposable.add(dispose)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        categoriesRepository.onClear()
    }
}