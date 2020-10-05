package com.sehentak.base.model

import com.google.gson.annotations.SerializedName

data class TokenMdl(
    @SerializedName("currentToken")
    var current: String? = null,
    @SerializedName("refreshToken")
    var refresh: String? = null,
    @SerializedName("jwt")
    var jwt: String? = current,
    @SerializedName("fcm")
    var fcm: String? = null
)