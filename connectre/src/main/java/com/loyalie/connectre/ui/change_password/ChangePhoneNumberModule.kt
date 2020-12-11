package com.loyalie.connectre.ui.change_password

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ChangePhoneNumberModule {

    @Binds
    @IntoMap
    @ViewModelKey(ChangePhoneVM::class)
    abstract fun bindChangePhoneVM(viewModel: ChangePhoneVM): ViewModel
}