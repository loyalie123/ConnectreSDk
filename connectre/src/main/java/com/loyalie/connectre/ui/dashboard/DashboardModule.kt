package com.loyalie.connectre.ui.dashboard

import androidx.lifecycle.ViewModel
import com.loyalie.connectre.di.ViewModelKey
import com.loyalie.connectre.ui.dashboard.payment.PaymentFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class DashboardModule {
    @Binds
    @IntoMap
    @ViewModelKey(DashboardVM::class)
    abstract fun bindSelectDeveloperVM(viewModel: DashboardVM): ViewModel
    @ContributesAndroidInjector
    internal abstract fun paymentFragmnet(): PaymentFragment
    @ContributesAndroidInjector
    internal abstract fun overViewFragemnt(): OverViewfragmnt

}