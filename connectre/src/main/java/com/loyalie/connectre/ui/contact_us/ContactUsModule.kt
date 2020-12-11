package com.loyalie.connectre.ui.contact_us

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ContactUsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsVM::class)
    abstract fun bindFAQVM(viewModel: ContactUsVM): ViewModel
}