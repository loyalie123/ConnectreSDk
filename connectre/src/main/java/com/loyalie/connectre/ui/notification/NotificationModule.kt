package com.loyalie.connectre.ui.notification

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import com.loyalie.connectre.ui.lead.LeadListFragment
import com.loyalie.connectre.ui.lead.LeadListVM
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class NotificationModule {

    /*   @Binds
       @IntoMap
       @ViewModelKey(NotificationVM::class)
       abstract fun bindNotificationVM(viewModel: NotificationVM): ViewModel*/

    @ContributesAndroidInjector
    internal abstract fun notificationFragment(): NotificationFragment

    @ContributesAndroidInjector
    internal abstract fun leadFragment(): LeadListFragment

    @Binds
    @IntoMap
    @ViewModelKey(NotificationVM::class)
    abstract fun bindBenefitsVM(viewModel: NotificationVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LeadListVM::class)
    abstract fun bindRewardsVM(viewModel: LeadListVM): ViewModel
}