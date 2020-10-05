package com.sehentak.base.helper

import android.util.Log
import com.google.gson.Gson
import com.sehentak.base.BuildConfig

/**
 * API for sending log output.
 *
 * @author  Angger Prasetyo
 * @since   2019 Aug 8
 * @see     android.util.Log
 */

object Log {

    fun debug(tag: String, data: Any?) {
        if (data != null) {
            debug(tag, Gson().toJson(data))
        } else debug(tag, "")
    }

    /**
     * This method only for debug
     * and showing as red text. In release mode, it just show
     * as info with white text on it.
     *
     * @param   tag Used to identify the source of a log message.
     *          It usually identifies the class or activity
     *          where the log call occurs.
     * @param   message The message you would like logged.
     */
    fun debug(tag: String, message: String?){
        if (BuildConfig.DEBUG) {
            Log.e(BuildConfig.KEY_PACKAGE, "$tag: $message")
        } else Log.i(BuildConfig.KEY_PACKAGE, tag)
    }

    fun debug(tag: String, e: Exception) {
        debug(tag, null, e)
    }

    fun debug(tag: String, message: String?, e: Exception) {
        if (BuildConfig.DEBUG) {
            Log.e(BuildConfig.KEY_PACKAGE, "$tag:${if (message != null) 
            " $message" else ""} ${e.message}")
            e.printStackTrace()
        } else Log.i(BuildConfig.KEY_PACKAGE, tag)
    }
}