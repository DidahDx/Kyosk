package com.github.didahdx.kyosk

import android.app.Application
import com.github.didahdx.kyosk.common.TimberLoggingTree
import timber.log.Timber

/**
 * Created by Daniel Didah on 10/20/21.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(TimberLoggingTree())
    }

}