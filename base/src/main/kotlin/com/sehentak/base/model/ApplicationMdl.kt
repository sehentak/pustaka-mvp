package com.sehentak.base.model

import com.google.gson.annotations.SerializedName

data class ApplicationMdl(
    @SerializedName("application_id")
    var id: String = "",
    @SerializedName("application_name")
    var name: String = "",
    @SerializedName("application_level")
    var level: Int? = null,
    @SerializedName("application_validation")
    var validation: Boolean = false
)