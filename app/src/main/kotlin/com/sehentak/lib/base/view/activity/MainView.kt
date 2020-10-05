package com.sehentak.lib.base.view.activity

import com.sehentak.base.base.BaseView
import com.sehentak.lib.base.model.VersionMdl

interface MainView: BaseView {
    fun onSuccessGetReleaseHistory(result: List<VersionMdl>)
}