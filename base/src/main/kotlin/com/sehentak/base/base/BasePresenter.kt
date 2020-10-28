package com.sehentak.base.base

import com.sehentak.base.helper.Log
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V>(var view: V): CustomDisposable {
    private val mTagClass = this::class.java.simpleName

    /**
     * A generic ordered collection of elements that supports adding and removing elements.
     */
    private var compositeDisposable: MutableList<Disposable> = mutableListOf()

    /**
     * Adds the specified element to the end of this list.
     */
    override fun addCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    /**
     * Dispose the resource, the operation should be idempotent.
     */
    override fun finishDisposable() {
        for (disposable in compositeDisposable) try {
            disposable.dispose()
        } catch (e: Exception) {
            Log.debug(mTagClass, "error: finishDisposable: ${e.message}")
        }
    }

    /**
     * Dispose the resource, the operation should be idempotent.
     */
    fun onDestroy() {
        finishDisposable()
    }
}