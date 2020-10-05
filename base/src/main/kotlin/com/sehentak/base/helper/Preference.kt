package com.sehentak.base.helper

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.sehentak.base.R
import com.sehentak.base.func.preferences.BasePreference
import com.sehentak.base.model.LocationMdl
import com.sehentak.base.model.TokenMdl

object Preference {
    @SuppressLint("StaticFieldLeak")
    private var instance: BasePreference? = null

    fun savePrimaryKey(context: Context?, primaryKey: String?) {
        if (context != null) {
            val pref = context.checkInstance()
            val key = pref.generatorKey(R.string.prop_pref_key)
            pref.edit.putString(key, primaryKey).apply()
        }
    }

    fun getPrimaryKey(context: Context?): String? {
        return if (context == null) null
        else {
            val pref = context.checkInstance()
            val key = pref.generatorKey(R.string.prop_pref_key)
            pref.use.getString(key, null)
        }
    }

    fun saveToken(context: Context?, token: TokenMdl?){
        if (context != null) {
            val pref = context.checkInstance()
            val key = pref.generatorKey(R.string.prop_pref_token)
            val data: String? = if (token == null) null
            else pref.encryptGson(token)
            pref.edit.putString(key, data).apply()
        }
    }

    fun getToken(context: Context?): TokenMdl?{
        return if (context == null) null
        else {
            val pref = context.checkInstance()
            val key = pref.generatorKey(R.string.prop_pref_token)
            val result = pref.use.getString(key, null)
            return if (result == null) null
            else pref.decryptGson(result, TokenMdl::class.java)
        }
    }

    fun saveUpdateLocation(context: Context?, location: LocationMdl?) {
        if (context != null) {
            val pref = context.checkInstance()
            val key = context.getString(R.string.prop_pref_location)
            val data: String? = if (location == null) null
            else Gson().toJson(location)
            pref.edit.putString(key, data).apply()
        }
    }

    fun getUpdateLocation(context: Context?): LocationMdl? {
        return if (context == null) null
        else {
            val pref = context.checkInstance()
            val key = context.getString(R.string.prop_pref_location)
            val result = pref.use.getString(key, null)
            if (result == null) null
            else Gson().fromJson(result, LocationMdl::class.java)
        }
    }

    private fun Context.checkInstance(): BasePreference {
        return if (instance != null) instance!!
        else BasePreference.getInstance(this)
    }
}