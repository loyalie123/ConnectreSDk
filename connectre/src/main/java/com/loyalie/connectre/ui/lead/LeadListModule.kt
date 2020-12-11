package com.loyalie.connectre.ui.lead

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LeadListModule {

    @Binds
    @IntoMap
    @ViewModelKey(LeadListVM::class)
    abstract fun bindLeadListVM(viewModel: LeadListVM): ViewModel
}