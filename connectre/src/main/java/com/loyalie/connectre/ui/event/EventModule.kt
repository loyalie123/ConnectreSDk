package com.loyalie.connectre.ui.event

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class EventModule {


    @Binds
    @IntoMap
    @ViewModelKey(EventVM::class)
    abstract fun bindEventVM(viewModel: EventVM): ViewModel
}