package com.github.didahdx.kyosk.di.components

import dagger.Module

/**
 * Created by Daniel Didah on 10/28/21.
 */
@Module(
    subcomponents = [
        ActivitySubComponent::class,
        FragmentSubComponent::class
    ]
)
class AppSubComponents