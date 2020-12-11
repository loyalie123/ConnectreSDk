package com.loyalie.connectre.ui.developers

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class SelectDeveloperModule {
    @Binds
    @IntoMap
    @ViewModelKey(SelectDeveloperVM::class)
    abstract fun bindSelectDeveloperVM(viewModel: SelectDeveloperVM): ViewModel
}