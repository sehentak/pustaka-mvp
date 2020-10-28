package com.sehentak.base.api

import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.BaseMdl
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created on 23/06/20.
 * @author <a href="mailto:angger@bojoo.com">Angger Prasetyo</a>
 */
interface ApiServices {

    @GET("cdn-cgi/trace")
    /**
     * We compose client IP with some details
     * This function use external providers (cloudflare)
     * Please check the result, or use [com.sehentak.base.model.PlatformMdl]
     * @return [ResponseBody]
     */
    fun getIpAddress(): Observable<ResponseBody>

    @GET("application/challenge")
    /**
     * Challenge application with some detail(s)
     * This function purpose is to validate your app with our SDK
     * @param token [String] SehentakToken
     * @param ip [String] client IP address
     * @param applicationVersion [String] sehentak app version
     * @param platformType [String] platform type
     * @param platformVersion [String] platform version
     * @param deviceBrand [String] client device brand
     * @param deviceModel [String] client device model
     * @param deviceSerial [String] client device serial
     * @param deviceType [Int] client device type
     *
     * @return [ApplicationMdl]
     */
    fun getChallenge(
        @Query("token") token: String,
        @Query("ip") ip: String?,
        @Query("application_version") applicationVersion: String?,
        @Query("platform_type") platformType: String?,
        @Query("platform_version") platformVersion: String?,
        @Query("device_brand") deviceBrand: String?,
        @Query("device_model") deviceModel: String?,
        @Query("device_serial") deviceSerial: String?,
        @Query("device_type") deviceType: Int
    ): Observable<BaseMdl<ApplicationMdl>>
}