package com.sehentak.base.repo

import io.reactivex.Observable
import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.PlatformMdl

interface BaseReq {
    fun getIpPlatform(): Observable<PlatformMdl>
    fun challenge(appVersion: String, platform: PlatformMdl?): Observable<ApplicationMdl>
}