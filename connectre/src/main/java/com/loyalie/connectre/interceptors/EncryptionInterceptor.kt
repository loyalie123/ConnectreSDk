/*
package com.happinest.happiedge.interceptors

import android.util.Log
import com.happinest.happiedge.crypto.CryptoStrategy
import com.happinest.happiedge.crypto.CryptoUtil
import okhttp3.*
import java.io.IOException

class EncryptionInterceptor(var mEncryptionStrategy: CryptoStrategy?, var mDecryptionStrategy:CryptoStrategy) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("OkHttp","===============ENCRYPTING REQUEST===============")
        var request: Request = chain.request()
        val rawBody: RequestBody? = request.body()
        var encryptedBody = ""
        val mediaType: MediaType = MediaType.parse("text/plain; charset=utf-8")!!
        if (mEncryptionStrategy != null) {
            try {
                val rawBodyString: String = CryptoUtil.requestBodyToString(rawBody)
                encryptedBody = mEncryptionStrategy!!.encrypt(rawBodyString)
                Log.d("OkHttpRaw body=> %s", rawBodyString)
                Log.d("OkHttpEncrypted=> %s", encryptedBody)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            throw IllegalArgumentException("No encryption strategy!")
        }
        //            InputStream cryptedStream = response.body().byteStream();
      */
/*  val responseStr: String =encryptedBody


        var decryptedString: String? = null
        if (mDecryptionStrategy != null) {
            try {
                decryptedString = mDecryptionStrategy.decrypt(responseStr)
                Log.d("OkHttpDecryptedstr=> %s", decryptedString)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
//
//                Log.d("Decrypted BODY", decryptedString);
        } else {
            throw java.lang.IllegalArgumentException("No decryption strategy!")
        }*//*

        val body: RequestBody = RequestBody.create(mediaType, encryptedBody)

        //build new request
        request = request.newBuilder()
                .header("Content-Type", body.contentType().toString())
                .header("Content-Length", java.lang.String.valueOf(body.contentLength()))
                .method(request.method(), body).build()
        return chain.proceed(request)
    }

    companion object {
        private val TAG = EncryptionInterceptor::class.java.simpleName
    }

    //injects the type of encryption to be used
    init {
        this.mEncryptionStrategy=mEncryptionStrategy
//        this.mDecryptionStrategy=mDecryptionStrategy
    }
}*/
