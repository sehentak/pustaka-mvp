@file:Suppress("DEPRECATION")
package com.sehentak.base.func.preferences

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sehentak.base.R
import com.sehentak.base.func.encryption.BaseCrypto
import java.lang.reflect.Type

class BasePreference: OnSharedPreferences {
    private lateinit var context: Context
    private lateinit var baseCrypto: BaseCrypto

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private lateinit var pref: BasePreference

        fun getInstance(context: Context): BasePreference {
            pref = BasePreference()
            pref.context = context
            pref.baseCrypto = BaseCrypto.getInstance(context)
            return pref
        }
    }

    override val use get() = sharedPreferences(context)

    override val edit: SharedPreferences.Editor get() = use.edit()

    override fun generatorKey(resourceKey: Int): String {
        return generatorKey(context, resourceKey)
    }

    override fun encryptString(data: String): String? {
        return baseCrypto.encrypt(data)
    }

    override fun decryptString(data: String): String? {
        return baseCrypto.decrypt(data)
    }

    override fun encryptGson(data: Any): String? {
        return baseCrypto.encrypt(Gson().toJson(data))
    }

    override fun <T> decryptGson(data: String, classOfT: Class<T>): T {
        val result = baseCrypto.decrypt(data)
        return Gson().fromJson(result, classOfT)
    }

    override fun <T> decryptGson(data: String, token: Type): T {
        val result = baseCrypto.decrypt(data)
        return Gson().fromJson(result, token)
    }

    override fun <T> decryptListGson(data: String, classListOfT: Class<T>): T {
        val result = baseCrypto.decrypt(data)
        val type = object : TypeToken<T>() {}.type
        return Gson().fromJson(result, type)
    }


    private fun sharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @SuppressLint("HardwareIds")
    @Suppress("DEPRECATION")
    private fun generatorKey(context: Context, resourceKey: Int): String {
        val key = "${Build.SERIAL}.${context.getString(resourceKey)}"
        val dev = context.getString(R.string.app_dev)
        val byte = "$dev$key".toByteArray(Charsets.UTF_8)
        return Base64.encodeToString(byte, Base64.NO_WRAP)
    }
}