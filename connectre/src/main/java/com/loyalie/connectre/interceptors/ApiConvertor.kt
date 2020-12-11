package com.loyalie.connectre.interceptors

import com.google.gson.reflect.TypeToken
import com.loyalie.connectre.data.ApiException
import com.loyalie.connectre.data.ApiResponse2
import com.loyalie.connectre.interceptors.Retrofitter.preferenceStorage
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.logout
import retrofit2.Converter
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.io.IOException


class ApiConvertorFactory : Converter.Factory() {
    override fun responseBodyConverter(
            type: Type,
            annotations: Array<Annotation>,
            retrofit: Retrofit): Converter<ResponseBody, *> {
        val envelopeType = TypeToken.getParameterized(ApiResponse2::class.java, type).type
        val delegate: Converter<ResponseBody, ApiResponse2<Any>> = retrofit.nextResponseBodyConverter(this, envelopeType, annotations)
        return ApiConvertor(delegate)
    }
}

internal class ApiConvertor(val delegate: Converter<ResponseBody, ApiResponse2<Any>>) : Converter<ResponseBody, Any> {

    @Throws(IOException::class)
    override fun convert(responseBody: ResponseBody): Any {
        val envelope = delegate.convert(responseBody)
        if (envelope?.tokenStatus == 1 && envelope.status == 1) return envelope.result
        else if (envelope?.tokenStatus == 2 || envelope?.tokenStatus ==0) {
            ConnectReApp.instance.logout(preferenceStorage,true)
            throw ApiException(AUTH_ERROR, envelope.message)
        } else throw ApiException(envelope!!.status, envelope!!.message)
    }
}