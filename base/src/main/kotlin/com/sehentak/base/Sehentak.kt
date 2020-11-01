package com.sehentak.base

import android.app.Application
import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.security.NetworkSecurityPolicy

class Sehentak: Application() {
    private lateinit var instanceSecurity: NetworkSecurityPolicy

    companion object {
        private lateinit var context: Context
        private var isDebug = false

        var isApplicationDebug: Boolean
            get() = this.isDebug
            set(value) {
                this.isDebug = value
            }

        var mContext: Context
            get() = this.context
            set(value) {
                this.context = value
            }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        if (VERSION.SDK_INT >= VERSION_CODES.N) {
            mNetworkSecurity?.isCleartextTrafficPermitted(isDebug.toString())
        }
    }

    private val mNetworkSecurity: NetworkSecurityPolicy? get() {
        return if (!::instanceSecurity.isInitialized && VERSION.SDK_INT >= VERSION_CODES.M) {
            instanceSecurity = NetworkSecurityPolicy.getInstance()
            instanceSecurity
        } else null
    }
}