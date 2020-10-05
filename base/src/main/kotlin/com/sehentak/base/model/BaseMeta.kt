package com.sehentak.base.model

import com.google.gson.annotations.SerializedName

data class BaseMeta(
    @SerializedName("response_at")
    var responseAt: String? = null,
    @SerializedName("total")
    var total: Int? = null,
    @SerializedName("count")
    var count: Int? = null,
    @SerializedName("page")
    var page: Int? = null,
    @SerializedName("per_page")
    var perPage: Int? = null,
    @SerializedName("last_page")
    var lastPage: Int? = null
)