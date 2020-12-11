package com.loyalie.connectre.ui.offer

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class OfferModule {

    @Binds
    @IntoMap
    @ViewModelKey(OfferVM::class)
    abstract fun bindOfferVM(viewModel: OfferVM): ViewModel
}