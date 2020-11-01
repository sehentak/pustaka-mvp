package com.sehentak.base

import android.app.Application

class Sehentak: Application() {

    companion object {
        private var isDebug = false

        var isApplicationDebug: Boolean
            get() = this.isDebug
            set(value) {
                isDebug = value
            }
    }
}