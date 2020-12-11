package com.loyalie.connectre.ui.refer_contact

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ReferContactModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReferContactVM::class)
    abstract fun bindReferContactVM(viewModel: ReferContactVM): ViewModel
}