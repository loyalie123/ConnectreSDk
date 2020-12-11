package com.loyalie.connectre.di

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.data.SharedPreferenceStorage
import com.loyalie.connectre.data.UserIndependantPreferenceStore
import com.loyalie.connectre.data.UserIndependentPref
import com.loyalie.connectre.network.ConnectreService
import com.loyalie.connectre.network.HeaderInterceptor
import com.loyalie.connectre.util.AppRxSchedulers
import com.loyalie.connectre.util.BASE_URL
import com.loyalie.connectre.util.ConnectReApp
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideContext(application: ConnectReApp): Context = application.applicationContext


    @Singleton
    @Provides
    fun provideRxSchedulers(): AppRxSchedulers = AppRxSchedulers(
        io = Schedulers.io(),
        computation = Schedulers.computation(),
        main = AndroidSchedulers.mainThread()
    )

    @Provides
    @Singleton
    fun retrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(callAdapterFactory)
            .build()
    }



    @Provides
    @Singleton
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun callAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Provides
    @Singleton
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun okHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        headerInterceptor: HeaderInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(headerInterceptor)

            .connectTimeout(0, TimeUnit.SECONDS)
            .readTimeout(0, TimeUnit.SECONDS)
            .writeTimeout(0, TimeUnit.SECONDS)
            .build()
    }


    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
//        Log.d("OkHttp", logging.toString())
        return logging
    }

    @Provides
    @Singleton
    fun headerInterceptor(vPrefs: PreferenceStorage): HeaderInterceptor {
        return HeaderInterceptor(vPrefs)
    }



    @Provides
    @Singleton
    fun ivfService(retrofit: Retrofit): ConnectreService {
        return retrofit.create(ConnectreService::class.java)
    }




    @Singleton
    @Provides
    fun providesPreferenceStorage(context: ConnectReApp): PreferenceStorage =
        SharedPreferenceStorage(context)

    @Singleton
    @Provides
    fun providesUserIndependedntPref(context: ConnectReApp): UserIndependentPref =
        UserIndependantPreferenceStore(context)

    @Provides
    @Singleton
    fun picasso(): Picasso {
        return Picasso.get()
    }

    @Provides
    @Singleton
    fun providePushNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


}