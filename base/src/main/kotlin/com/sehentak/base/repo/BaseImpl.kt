package com.sehentak.base.repo

import android.annotation.SuppressLint
import android.os.Build
import android.util.Base64
import io.reactivex.Observable
import com.sehentak.base.BuildConfig
import com.sehentak.base.api.ApiClient
import com.sehentak.base.api.ApiServices
import com.sehentak.base.helper.getSerialNumber
import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.PlatformMdl
import java.util.*

class BaseImpl: BaseReq {
    private val apiSehentak = ApiClient.init(ApiServices::class.java, BuildConfig.URL_BASE)
    private val apiCloudFlare = ApiClient.init(ApiServices::class.java, BuildConfig.URL_CLOUDFLARE)

    override fun getIpPlatform(): Observable<PlatformMdl> {
        return apiCloudFlare.getIpAddress().flatMap {
            val string = it.string()
            val data = PlatformMdl()
            data.ipAddress = if (string.contains("ip=") && string.contains("ts=")) {
                val parse1 = string.split("ip=")[1]
                parse1.split("ts=")[0].trim()
            } else null

            val platform = if (string.contains("uag=") && string.contains("colo=")) {
                var parse2 = string.split("uag=")[1]
                parse2.split("colo=")[0].trim()
                if (parse2.contains(" ")) {
                    val count = parse2.split(" ").count()
                    parse2 = parse2.split(" ")[count - 1]
                }
                parse2
            } else null

            var type: String? = null
            var version: String? = null
            if (platform != null && platform.contains("/")) {
                type = platform.split("/")[0]
                version = platform.split("/")[1]
            }
            data.platformType = type
            data.platformVersion = version

            Observable.just(data)
        }
    }

    @SuppressLint("HardwareIds")
    override fun challenge(appVersion: String, platform: PlatformMdl?): Observable<ApplicationMdl> {
        val combine = "${Date().time}:${BuildConfig.API_ID}shntk${BuildConfig.API_KEY}"
        val bytes = combine.toByteArray()
        var token = Base64.encodeToString(bytes, Base64.DEFAULT)
        token = token.replace("\\s".toRegex(), "")
        var version = appVersion
        if (version.contains(" ")) version = version.split(" ")[0]

        return apiSehentak.getChallenge(
            token,
            platform?.ipAddress,
            "v$version",
            "Android",
            "v${Build.VERSION.SDK_INT}",
            Build.BRAND,
            Build.MODEL,
            getSerialNumber(),
            if (BuildConfig.DEBUG) 0 else 1
        ).flatMap {
            Observable.just(it.data)
        }
    }
}