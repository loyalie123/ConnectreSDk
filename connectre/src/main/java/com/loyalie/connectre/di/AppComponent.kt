package com.loyalie.connectre.di

import com.loyalie.connectre.util.ConnectReApp
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class/*,AppModuleForPayment:: class*/,
        ActivityBindingModule::class,
        ViewModelModule::class,
        ServiceBuilderModule::class]
)


interface AppComponent : AndroidInjector<ConnectReApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ConnectReApp>()
}

/*
interface AppComponentPaym : AndroidInjector<ConnectReApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ConnectReApp>()
}*/
