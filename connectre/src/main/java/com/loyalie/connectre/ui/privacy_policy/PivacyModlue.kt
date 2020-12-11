package com.loyalie.connectre.ui.privacy_policy

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class PrivacyModule {
    @Binds
    @IntoMap
    @ViewModelKey(PrivacyVM::class)
    abstract fun bindPrivacyVM(privacyVM: PrivacyVM): ViewModel
}