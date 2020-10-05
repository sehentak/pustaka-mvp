package com.sehentak.lib.base.repo

import com.sehentak.base.BuildConfig
import com.sehentak.base.api.ApiClient
import com.sehentak.lib.base.api.ApiServices
import com.sehentak.lib.base.model.VersionMdl
import io.reactivex.Observable

class RepoImpl: RepoReq {
    private val api = ApiClient.init(ApiServices::class.java, BuildConfig.URL_BASE)
    private val agent = "Android:LIB:${BuildConfig.API_ID}"

    override fun getReleaseHistory(): Observable<List<VersionMdl>> {
        return api.getBaseVersionHistory(agent, BuildConfig.API_ID, BuildConfig.API_KEY).flatMap {
            Observable.just(it.data)
        }
    }
}