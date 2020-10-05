package com.sehentak.base.model

import com.google.gson.annotations.SerializedName

data class BaseMdl<T>(
    @SerializedName("status")
    var status: Int = 204,
    @SerializedName("message")
    var message: String = "No massage",
    @SerializedName("data")
    var data: T? = null,
    @SerializedName("meta")
    var meta: BaseMeta? = null
)