package com.sehentak.base.func.preferences

import android.content.SharedPreferences
import java.lang.reflect.Type

interface OnSharedPreferences {
    val use: SharedPreferences
    val edit: SharedPreferences.Editor
    fun generatorKey(resourceKey: Int): String
    fun encryptString(data: String): String?
    fun decryptString(data: String): String?
    fun encryptGson(data: Any): String?
    fun <T> decryptGson(data: String, classOfT: Class<T>): T
    fun <T> decryptGson(data: String, token: Type): T
    fun <T> decryptListGson(data: String, classListOfT: Class<T>): T
}