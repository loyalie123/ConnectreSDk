package com.loyalie.connectre.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ConnectReVMFactory):
            ViewModelProvider.Factory
}