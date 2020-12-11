package com.loyalie.connectre.network

import android.util.Log
import com.loyalie.connectre.data.PreferenceStorage
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val pref: PreferenceStorage)
    : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Accept", "application/json")
            ?.addHeader("sec_key",/* pref.authToken*/"aguu2Hw1P5QWMN6Vg1xlf/mFlpb+Gj248/+bhTvHryE=")

            ?.build()
//        Log.d("OkHttp",request.toString())
        return chain.proceed(request)
    }
}