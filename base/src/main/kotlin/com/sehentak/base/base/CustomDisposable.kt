package com.sehentak.base.base

import io.reactivex.disposables.Disposable

/**
 * Created by angger on 2019-08-07.
 */

interface CustomDisposable {
    fun addCompositeDisposable(disposable: Disposable)
    fun finishDisposable()
}