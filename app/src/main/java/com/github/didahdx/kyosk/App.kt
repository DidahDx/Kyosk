package com.github.didahdx.kyosk

import android.app.Application
import com.github.didahdx.kyosk.common.TimberLoggingTree
import com.github.didahdx.kyosk.di.components.AppComponent
import com.github.didahdx.kyosk.di.components.DaggerAppComponent

import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Daniel Didah on 10/20/21.
 */
class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(TimberLoggingTree())
        appComponent = DaggerAppComponent.builder()
            .application(this).build()
        appComponent.inject(this)
    }

}