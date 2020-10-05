package com.sehentak.base.func.encryption

interface OnCryptoExecute{
    fun encrypt(data: String?): String?
    fun decrypt(data: String?): String?
    fun encrypt(key: String?, data: String?): String?
    fun decrypt(key: String?, data: String?): String?
}