package com.loyalie.connectre.di

import com.loyalie.connectre.fcm.ConnectreFCMService
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ServiceBuilderModule {

    @ContributesAndroidInjector
    internal abstract fun contributeFCMService(): ConnectreFCMService
}