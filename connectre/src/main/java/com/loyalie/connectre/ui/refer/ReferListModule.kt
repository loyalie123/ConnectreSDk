package com.loyalie.connectre.ui.refer

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ReferListModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReferListVM::class)
    abstract fun bindHomeVM(viewModel: ReferListVM): ViewModel
}