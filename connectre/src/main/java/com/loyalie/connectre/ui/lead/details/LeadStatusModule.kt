package com.loyalie.connectre.ui.lead.details

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LeadStatusModule {

    @Binds
    @IntoMap
    @ViewModelKey(LeadStatusVM::class)
    abstract fun bindHomeVM(viewModel: LeadStatusVM): ViewModel
}