package com.sehentak.base.api

import com.sehentak.base.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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

        if (BuildConfig.DEBUG) client.addInterceptor(HttpLoggingInterceptor().apply {
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