package com.sehentak.base.base

import io.reactivex.disposables.Disposable

/**
 * Represents a disposable resource.
 *
 * @author Angger Prasetyo - angger@sehentak.com
 * @since 2019-08-07
 */
interface CustomDisposable {
    fun addCompositeDisposable(disposable: Disposable)
    fun finishDisposable()
}