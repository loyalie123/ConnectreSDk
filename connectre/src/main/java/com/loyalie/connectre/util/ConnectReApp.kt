package com.loyalie.connectre.util

import androidx.core.content.ContextCompat
//import com.crashlytics.android.Crashlytics
import com.loyalie.connectre.R
import com.loyalie.connectre.di.DaggerAppComponent
import com.loyalie.connectre.interceptors.Retrofitter
import com.loyalie.connectre.network.ConnectreService
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

//import io.fabric.sdk.android.Fabric

class ConnectReApp  : DaggerApplication() {

    companion object {

        lateinit var instance: ConnectReApp private set



     val networkService: ConnectreService by lazy {
            Retrofitter.getService ()
        }
        @JvmStatic
        var themeColor = 0

        @JvmStatic
        var vendorID = 0
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        themeColor = ContextCompat.getColor(this, R.color.black)
        vendorID = 0
//        Fabric.with(this, Crashlytics())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }


}

