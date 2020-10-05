package com.sehentak.base.api

import io.reactivex.Observable
import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.BaseMdl
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created on 23/06/20.
 * @author <a href="mailto:angger@bojoo.com">Angger Prasetyo</a>
 */
interface ApiServices {

    @GET("cdn-cgi/trace")
    fun getIpAddress(): Observable<ResponseBody>

    @GET("application/challenge")
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