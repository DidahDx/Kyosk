package com.github.didahdx.kyosk.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Daniel Didah on 10/26/21.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @Test
    fun appLaunchesWithoutCrash() {
        ActivityScenario.launch(MainActivity::class.java)
    }

}