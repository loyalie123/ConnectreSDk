package com.loyalie.connectre.ui.enter_phn

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class EnterPhoneModule {
    @Binds
    @IntoMap
    @ViewModelKey(EnterPhoneVM::class)
    abstract fun bindEnterPhoneVM(viewModel: EnterPhoneVM): ViewModel

}