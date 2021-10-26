package com.github.didahdx.kyosk.ui


import androidx.fragment.app.Fragment
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import com.github.didahdx.kyosk.di.InjectedSavedStateViewModelFactory
import javax.inject.Inject

abstract class BaseFragment : Fragment(), HasDefaultViewModelProviderFactory {
    /**
     * dagger.Lazy used here, so that injection is request when [getDefaultViewModelProviderFactory] is called
     */
    @Inject
    lateinit var defaultViewModelFactory: dagger.Lazy<InjectedSavedStateViewModelFactory>

    /**
     * This method androidx uses for `by viewModels` method.
     * We can set out injecting factory here and therefore don't touch it again
     */
    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        defaultViewModelFactory.get().create(this, arguments)
}
