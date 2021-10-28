package com.github.didahdx.kyosk.ui.catergory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.common.Resources
import com.github.didahdx.kyosk.data.repository.CategoryRepository
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
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val item = MutableLiveData<Resources<List<RecyclerViewItems>>>()

    fun getAllProductByCategory(categoryTitle: RecyclerViewItems.CategoryTitle) {
        compositeDisposable += categoryRepository.fetchAllProductsByCategory(categoryTitle)
            .subscribeOn(Schedulers.io())
            .throttleLast(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                item.value = it
            }, { error ->
                Timber.e(error)
                item.value = Resources.Error<List<RecyclerViewItems>>("Something went wrong")
            })
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        categoryRepository.onClear()
    }
}