package com.github.didahdx.kyosk.di.components

import com.github.didahdx.kyosk.di.FragmentScope
import com.github.didahdx.kyosk.di.modules.ViewModelModule
import com.github.didahdx.kyosk.ui.categories.CategoriesFragment
import com.github.didahdx.kyosk.ui.catergory.CategoryFragment
import com.github.didahdx.kyosk.ui.home.HomeFragment
import com.github.didahdx.kyosk.ui.product_details.ProductDetailFragment
import dagger.Subcomponent

/**
 * Created by Daniel Didah on 10/27/21.
 */
@FragmentScope
@Subcomponent(modules = [ViewModelModule::class])
interface FragmentSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): FragmentSubComponent
    }

    fun inject(homeFragment: HomeFragment)
    fun inject(categoriesFragment: CategoriesFragment)
    fun inject(categoryFragment: CategoryFragment)
    fun inject(productDetailFragment: ProductDetailFragment)
}