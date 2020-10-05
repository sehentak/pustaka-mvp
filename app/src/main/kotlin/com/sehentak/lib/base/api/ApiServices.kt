package com.sehentak.lib.base.api

import com.sehentak.base.model.BaseMdl
import com.sehentak.lib.base.model.VersionMdl
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiServices {

    @GET("base")
    fun getBaseVersionHistory(
        @Header("User-Agent") agent: String,
        @Query("app_id") applicationId: String,
        @Query("app_key") applicationKey: String
    ): Observable<BaseMdl<List<VersionMdl>>>
}