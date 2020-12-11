package com.loyalie.connectre.ui.feedback

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class FeedbackModule {
    @Binds
    @IntoMap
    @ViewModelKey(FeedbackVM::class)
    abstract fun bindFeedbackVM(viewModel: FeedbackVM): ViewModel

}