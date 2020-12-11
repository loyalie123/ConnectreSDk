package com.loyalie.connectre.interceptors

import android.os.Build
import android.util.Log
import com.happinest.happiedge.misc.DecryptionImpl
import com.loyalie.connectre.data.PreferenceStorage
import com.loyalie.connectre.data.getApiToken
import com.loyalie.connectre.network.ConnectreService
import com.loyalie.connectre.util.ConnectReApp
import com.loyalie.connectre.util.toast
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


object Retrofitter  {

    val hostname = "happiedge-uat.mahindrahappinest.com"
//val hostname="sni.cloudflaressl.com"
//    Demo dev

//    private const val BASE_URL_TEST_FOC = "http://13.127.63.66:8080/WinnRE/"
//    private const val BASE_URL_TEST_FOC = "http://18.140.57.92:8080/WinnRE/"
//    private const val BASE_URL_TEST_FOC = "http://54.255.136.91:8080/WinnRE/"
//    private const val BASE_URL_TEST_FOC = "http://13.235.128.96:8080/WinnRE/"
//      private const val BASE_URL_TEST_FOC = "http://54.255.136.91:8080/WinnRE/"
    /**client correct url**/

    private const val BASE_URL_TEST_FOC = "http://54.255.136.91:8080/winnre_shapoorji/"
//    private const val BASE_URL_TEST_FOC = "http://13.235.98.76:8080/WinnRE/"
//    private const val BASE_URL_TEST_FOC = "http://13.235.98.76:8080/WinnRE/"
    //UAT build url
//    private const val BASE_URL_TEST_FOC = "https://happiedge-uat.mahindrahappinest.com/WinnRE/"
//    private const val BASE_URL_TEST_FOC = "https://happiedge.mahindrahappinest.com/WinnRE-V2/"
    /**client production url**/
//    private const val BASE_URL_TEST_FOC = "https://happiedge.mahindrahappinest.com/WinnRE/"

//    var encryptionInterceptor: EncryptionInterceptor? = EncryptionInterceptor(EncryptionImpl(),DecryptionImpl())

    //Decryption Interceptor
    var decryptionInterceptor: DecryptionInterceptor? = null


    var k = ""
    @set:Inject
    lateinit var preferenceStorage: PreferenceStorage
    fun getService (): ConnectreService {

        decryptionInterceptor = DecryptionInterceptor(DecryptionImpl())
        val httpClient = OkHttpClient.Builder()
        if(decryptionInterceptor!=null){
            httpClient.readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
//                .certificatePinner(certificatePinner)
//                .addInterceptor(encryptionInterceptor)
                    // interceptor for decryption of request data
                    .addInterceptor(decryptionInterceptor)
                    .addInterceptor(HeaderInterceptor())
        }else{
            "Unexpected server error occured".toast(ConnectReApp.instance)
        }
//        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)
//            Log.d("OkHttp", logging.toString())
//        }
        enableTlsOnKitkat(httpClient)
        val retrofit: Retrofit = Retrofit
                .Builder()
                .baseUrl(BASE_URL_TEST_FOC)
            .addConverterFactory(ApiConvertorFactory())
            .addConverterFactory(GsonConverterFactory.create())

//                .client(SelfSigningClientBuilder.createClient(k))
//                ..........ssl pinning................
                .client(httpClient.build())
                .build()
        return retrofit.create(ConnectreService::class.java)
    }


    class HeaderInterceptor ()
        : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            request = request?.newBuilder()
                ?.addHeader("Accept", "application/json")
                ?.addHeader("sec_key", /*ConnectReApp.instance.getApiToken()*/"aguu2Hw1P5QWMN6Vg1xlf/mFlpb+Gj248/+bhTvHryE=")
                ?.addHeader("Content-Type","application/x-www-form-urlencoded")
                ?.build()

            return chain.proceed(request)
        }
    }



    private fun enableTlsOnKitkat(okHttp: OkHttpClient.Builder) {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm())
                trustManagerFactory.init(null as KeyStore?)
                val trustManagers = trustManagerFactory.getTrustManagers()
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
                }
                val trustManager = trustManagers[0] as X509TrustManager
                okHttp.sslSocketFactory(TlsSocketFactory(), trustManager)
            }
        } catch (e: Exception) {
            "Security issues found, Cannot perform the operation".toast(ConnectReApp.instance)
        }
    }
}
