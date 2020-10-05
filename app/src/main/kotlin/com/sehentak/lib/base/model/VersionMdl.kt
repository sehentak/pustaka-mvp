package com.sehentak.lib.base.model

import com.google.gson.annotations.SerializedName

data class VersionMdl(
    @SerializedName("version_name")
    var name: String = "-",
    @SerializedName("version_code")
    var code: Int = 0,
    @SerializedName("version_description")
    var description: String? = null,
    @SerializedName("release_commit")
    var commit: String? = null,
    @SerializedName("release_author")
    var author: String? = null,
    @SerializedName("release_email")
    var email: String? = null,
    @SerializedName("release_avatar")
    var avatar: String? = null,
    @SerializedName("release_date")
    var date: String? = null
)