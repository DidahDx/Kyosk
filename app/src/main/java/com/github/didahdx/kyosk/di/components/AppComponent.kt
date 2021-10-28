package com.github.didahdx.kyosk.di.components

import android.app.Application
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.di.modules.ApiServiceModule
import com.github.didahdx.kyosk.di.modules.CommonUiModule
import com.github.didahdx.kyosk.di.modules.DbModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Daniel Didah on 10/22/21.
 */
@Component(modules = [ApiServiceModule::class, AppSubComponents::class, DbModule::class, CommonUiModule::class])
@Singleton
interface AppComponent {

    fun getActivityComponentFactory(): ActivitySubComponent.Factory
    fun getFragmentComponentFactory(): FragmentSubComponent.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}