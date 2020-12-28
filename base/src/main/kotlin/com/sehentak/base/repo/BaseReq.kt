package com.sehentak.base.repo

import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.PlatformMdl
import io.reactivex.Observable

interface BaseReq {
    fun getIpPlatform(): Observable<PlatformMdl>
    fun challenge(appVersion: String, platform: PlatformMdl?): Observable<ApplicationMdl>
    fun challenge(appId: String, appKey: String, appVersion: String, platform: PlatformMdl?): Observable<ApplicationMdl>
}