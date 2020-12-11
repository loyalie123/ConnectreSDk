package com.loyalie.connectre.ui.otp

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class OtpModule {

    @Binds
    @IntoMap
    @ViewModelKey(OtpVM::class)
    abstract fun bindOtpVM(viewModel: OtpVM): ViewModel

}