package com.sehentak.base.func.start

import com.sehentak.base.base.BaseView
import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.PlatformMdl

interface StartView: BaseView {
    fun onSuccessGetPlatform(result: PlatformMdl)
    fun onSuccessBeatTheHeart(result: ApplicationMdl)
}