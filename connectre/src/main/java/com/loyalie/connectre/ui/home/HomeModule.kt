package com.loyalie.connectre.ui.home

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class HomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeVM::class)
    abstract fun bindHomeVM(viewModel: HomeVM): ViewModel
}