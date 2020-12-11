package com.loyalie.connectre.interceptors

import android.text.TextUtils
import android.util.Log
import com.loyalie.connectre.misc.CryptoStrategy
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody


class DecryptionInterceptor ( private val mDecryptionStrategy: CryptoStrategy?)
    : Interceptor
{
    var v1 = ""
    var response: Response? = null
    override fun intercept(chain: Interceptor.Chain?): Response? {
        if (chain != null) {
            response = chain.proceed(chain.request())
            if (response != null) {
                if (response?.isSuccessful!!) {
                    val newResponse: Response.Builder? = response?.newBuilder()
                    var contentType = response?.header("Content-Type")
                    if (TextUtils.isEmpty(contentType)) contentType = "application/json"
                    // InputStream cryptedStream = response.body().byteStream();
                    val body = response!!.body()
                    //Warning: this method loads the requested bytes into memory. Most
                    // applications should set a modest limit on {@code byteCount}, such as 1 MiB.
                    //Warning: this method loads the requested bytes into memory. Most
                    // applications should set a modest limit on {@code byteCount}, such as 1 MiB.
                    val bufferSize = 1024 * 1024
                    val responseStr = response!!.peekBody(bufferSize.toLong())?.string()/*"AWByFWNIdtFrUerLSMLrEpUF2wXOrEEoLLB8E7XVNlWB0c4CDUas8WV9u8zfl/qBzEDQ5pLThilR+O/+IOyIj6ZgpM/rsLey2r8Tl8sOPDC8N1DKZKwzbe5CX2g9vhTFc1LbYZQtPuQqTt++uNlRmebMAWNp+o0Zg/+LuhSCLXveOesOXhT0DOuerm1khrJQVeT+nHYuD4lgOw9sfHd4yc1c3G6mhneGyiHnRD1m74MM1jBN4AhD1ujDrLVSSgndHlKb9cRnO/5d2r2eBf0EYw=="*/
//                    val responseBody = response!!.body()
//                    val responseStr =  response?.peekBody(2048)?.string()
//            Log.d("OkHtResponse",responseStr!!)
                    v1 = "72e?5ve#7fa4*exi" ?: ""
                    var decryptedString: String = ""
                    if (mDecryptionStrategy != null) {
                        try {
                            if (responseStr != "" && responseStr != null) {

                                    decryptedString = mDecryptionStrategy.decrypt(responseStr, v1)!!
                            } else {
                                decryptedString = ""
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
//                        Log.d("OkHResponse=> %s", responseStr)
//                        Log.d("OkHDecrypted BODY", "" + decryptedString);
                    } else {
//                        throw IllegalArgumentException("No decryption strategy!")
                        decryptedString = ""
                    }
                    /*  val body = response.body()!!
                      val charset = contentType?.charset() ?: Charset.defaultCharset()
                      val buffer = body.source().apply { request(Long.MAX_VALUE) }.buffer()
                      val bodyContent = buffer.clone().readString(charset)
                      response.newBuilder()
                              .body(ResponseBody.create(contentType, bodyContent.let(::decryptBody)))
                              .build()*/
                    newResponse?.body(
                        ResponseBody.create(
                            MediaType.parse(contentType),
                            decryptedString
                        )
                    )
                    return newResponse?.build()
                }
            } else {
//                throw Exception("Unexpected server error occured")
                /*WingmanApp.instance.runOnUiThread {
                    "Unexpected server error occured".longToast(WingmanApp.instance)
                }*/
            }
        }
        return response
    }
}