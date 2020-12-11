package com.loyalie.connectre.ui.tutorial

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TutorialsListingModule {

    @Binds
    @IntoMap
    @ViewModelKey(TutorialsVM::class)
    abstract fun bindTutorialsVM(viewModel: TutorialsVM): ViewModel
}