package com.github.didahdx.kyosk.di.components

import android.app.Application
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.di.modules.ApiServiceModule
import com.github.didahdx.kyosk.di.modules.CommonUiModule
import com.github.didahdx.kyosk.di.modules.DbModule
import com.github.didahdx.kyosk.di.modules.ViewModelModule
import com.github.didahdx.kyosk.ui.MainActivity
import com.github.didahdx.kyosk.ui.categories.CategoriesFragment
import com.github.didahdx.kyosk.ui.catergory.CategoryFragment
import com.github.didahdx.kyosk.ui.home.HomeFragment
import com.github.didahdx.kyosk.ui.product_details.ProductDetailFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Component(modules = [ApiServiceModule::class, DbModule::class,
    ViewModelModule::class, CommonUiModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(productDetailFragment: ProductDetailFragment)
    fun inject(categoriesFragment: CategoriesFragment)
    fun inject(categoryFragment: CategoryFragment)
}