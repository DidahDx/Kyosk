package com.github.didahdx.kyosk.di.components

import com.github.didahdx.kyosk.di.ActivityScope
import com.github.didahdx.kyosk.ui.MainActivity
import dagger.Subcomponent

/**
 * Created by Daniel Didah on 10/27/21.
 */
@ActivityScope
@Subcomponent()
interface ActivitySubComponent {

    // Factory that is used to create instances of this subcomponent
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivitySubComponent
    }

    fun inject(mainActivity: MainActivity)

}