package com.sehentak.base.api

import android.annotation.SuppressLint
import com.sehentak.base.BuildConfig
import com.sehentak.base.Sehentak
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Create instances using [Retrofit.Builder.build] to generate an implementation.
 *
 * @author  Angger Prasetyo
 * @since   2019 Aug 9
 * @see     retrofit2
 */

object ApiClient {

    /**
     * Create an implementation of the API endpoints defined by the [services] interface.
     * The relative path for a given method is obtained from an annotation on the method
     * describing the request type.
     *
     * @param   services Services class that contains all request method
     * @param   baseUrl The specified endpoint values (such as with [retrofit2.http.GET] are
     *          resolved against this value using [okhttp3.HttpUrl]
     */
    fun <T> init(services: Class<T>, baseUrl: String): T {
        return init(services, baseUrl, BuildConfig.ENV_TIMEOUT_REQUEST)
    }

    /**
     * Create an implementation of the API endpoints defined by the [services] interface.
     * The relative path for a given method is obtained from an annotation on the method
     * describing the request type.
     *
     * @param   services Services class that contains all request method
     * @param   baseUrl The specified endpoint values (such as with [retrofit2.http.GET] are
     *          resolved against this value using [okhttp3.HttpUrl]
     * @param   isDownloadMode flagging to check what's implementation for.
     */
    fun <T> init(services: Class<T>, baseUrl: String, isDownloadMode: Boolean): T {
        return if (isDownloadMode) init(services, baseUrl, BuildConfig.ENV_TIMEOUT_DOWNLOAD)
        else init(services, baseUrl, BuildConfig.ENV_TIMEOUT_REQUEST)
    }

    /**
     * Create an implementation of the API endpoints defined by the [services] interface.
     * The relative path for a given method is obtained from an annotation on the method
     * describing the request type.
     *
     * @param   services Services class that contains all request method
     * @param   baseUrl The specified endpoint values (such as with [retrofit2.http.GET] are
     *          resolved against this value using [okhttp3.HttpUrl]
     * @param   timeOutInSeconds Sets the default read timeout for new connections.
     *          A value of 0 means no timeout, otherwise values must be between 1 and
     *          [Integer.MAX_VALUE] when converted to milliseconds.
     */
    fun <T> init(services: Class<T>, baseUrl: String, timeOutInSeconds: Long): T {
        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
        }

        client.readTimeout(timeOutInSeconds, TimeUnit.SECONDS)
        client.writeTimeout(timeOutInSeconds, TimeUnit.SECONDS)

        if (Sehentak.isApplicationDebug) client.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(services)
    }

    /**
     * Create an implementation of the API endpoints defined by the [services] interface.
     * Without SSL checker or every certificate issuer will be allowed here.
     * The relative path for a given method is obtained from an annotation on the method
     * describing the request type.
     *
     * @param   services Services class that contains all request method
     * @param   baseUrl The specified endpoint values (such as with [retrofit2.http.GET] are
     *          resolved against this value using [okhttp3.HttpUrl]
     */
    fun <T> initNonCertificate(services: Class<T>, baseUrl: String): T {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        val client = OkHttpClient.Builder()
        client.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        client.hostnameVerifier { _, _ -> true }

        client.apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            })
        }

        client.readTimeout(BuildConfig.ENV_TIMEOUT_REQUEST, TimeUnit.SECONDS)
        client.writeTimeout(BuildConfig.ENV_TIMEOUT_REQUEST, TimeUnit.SECONDS)

        if (Sehentak.isApplicationDebug) client.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(services)
    }
}