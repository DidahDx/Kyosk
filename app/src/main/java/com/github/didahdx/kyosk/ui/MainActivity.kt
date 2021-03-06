package com.github.didahdx.kyosk.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.github.didahdx.kyosk.App
import com.github.didahdx.kyosk.R
import com.github.didahdx.kyosk.databinding.ActivityMainBinding
import com.github.didahdx.kyosk.di.InjectedSavedStateViewModelFactory
import com.github.didahdx.kyosk.di.components.ActivitySubComponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var abstractViewModelFactory: dagger.Lazy<InjectedSavedStateViewModelFactory>

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        abstractViewModelFactory.get().create(this)

    lateinit var activitySubComponent: ActivitySubComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        activitySubComponent =
            (application as App).appComponent.getActivityComponentFactory().create()
        activitySubComponent.inject(this)
        setTheme(R.style.Theme_Kyosk)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup navigation component to bottom navigation bar
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }
}