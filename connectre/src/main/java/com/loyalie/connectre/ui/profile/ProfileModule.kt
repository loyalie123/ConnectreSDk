package com.loyalie.connectre.ui.profile

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ProfileModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileVM::class)
    abstract fun bindProfileVM(viewModel: ProfileVM): ViewModel
}