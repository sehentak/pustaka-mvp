package com.sehentak.base.base

interface BaseView {
    fun loadingStart(tag: String)
    fun loadingStop(tag: String)
    fun loadingError(tag: String, errorCode: Int, errorMessage: String?)
}