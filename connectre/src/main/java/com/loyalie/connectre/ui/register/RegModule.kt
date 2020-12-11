package com.loyalie.connectre.ui.register

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class RegModule {

    @Binds
    @IntoMap
    @ViewModelKey(RegVM::class)
    abstract fun bindRegVM(viewModel: RegVM): ViewModel
}