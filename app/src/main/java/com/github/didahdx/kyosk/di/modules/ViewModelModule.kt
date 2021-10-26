package com.github.didahdx.kyosk.di.modules

import androidx.lifecycle.ViewModel
import com.github.didahdx.kyosk.di.AssistedSavedStateViewModelFactory
import com.github.didahdx.kyosk.di.ViewModelKey
import com.github.didahdx.kyosk.ui.categories.CategoriesViewModel
import com.github.didahdx.kyosk.ui.catergory.CategoryViewModel
import com.github.didahdx.kyosk.ui.home.HomeViewModel
import com.github.didahdx.kyosk.ui.product_details.ProductDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
     abstract fun bindMainActivityViewModel(homeViewModel: HomeViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel::class)
    abstract fun bindProductDetailViewModel(productDetailViewModel: ProductDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(categoryViewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    abstract fun bindCategoriesViewModel(categoriesViewModel: CategoriesViewModel): ViewModel

}