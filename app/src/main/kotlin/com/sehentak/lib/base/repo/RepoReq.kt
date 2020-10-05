package com.sehentak.lib.base.repo

import com.sehentak.lib.base.model.VersionMdl
import io.reactivex.Observable

interface RepoReq {
    fun getReleaseHistory(): Observable<List<VersionMdl>>
}