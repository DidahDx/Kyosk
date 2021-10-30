package com.github.didahdx.kyosk.ui.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.repository.CategoriesRepository
import com.github.didahdx.kyosk.di.FragmentScope
import com.github.didahdx.kyosk.ui.home.RecyclerViewItems
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FragmentScope
class CategoriesViewModel @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) : ViewModel() {

    val categories = MutableLiveData<Resources<List<RecyclerViewItems.CategoryTitle>>>()
    private val compositeDisposable = CompositeDisposable()

    init {
        fetchAllCategories()
    }

    private fun fetchAllCategories() {
        compositeDisposable += categoriesRepository.fetchAll()
            .debounce(300,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ item ->
                Timber.e("$item")
                categories.value = item
            }, {
                Timber.e(it)
                categories.value =
                    Resources.Error<List<RecyclerViewItems.CategoryTitle>>("Something went wrong")
            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        categoriesRepository.onClear()
    }
}