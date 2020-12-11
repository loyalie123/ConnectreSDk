package com.loyalie.connectre.ui.faq

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FAQModule {

    @Binds
    @IntoMap
    @ViewModelKey(FAQVM::class)
    abstract fun bindFAQVM(viewModel: FAQVM): ViewModel
}