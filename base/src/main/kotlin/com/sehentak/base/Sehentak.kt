package com.sehentak.base

import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.security.NetworkSecurityPolicy

class Sehentak: Application() {
    private lateinit var instanceSecurity: NetworkSecurityPolicy

    companion object {
        private var isDebug = false

        var isApplicationDebug: Boolean
            get() = this.isDebug
            set(value) {
                isDebug = value
            }
    }

    override fun onCreate() {
        super.onCreate()

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