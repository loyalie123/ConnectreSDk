package com.loyalie.connectre.ui.rewards

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class RewardsModule {

    @ContributesAndroidInjector
    internal abstract fun rewardsFragment(): RewardsFragment

    @ContributesAndroidInjector
    internal abstract fun benefitsFragment(): BenefitsFragment

    @Binds
    @IntoMap
    @ViewModelKey(BenefitsVM::class)
    abstract fun bindBenefitsVM(viewModel: BenefitsVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RewardsVM::class)
    abstract fun bindRewardsVM(viewModel: RewardsVM): ViewModel

}