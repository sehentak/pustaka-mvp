package com.sehentak.base.base

import io.reactivex.disposables.Disposable
import com.sehentak.base.helper.Log

abstract class BasePresenter<V>(var view: V): CustomDisposable {
    private val mTagClass = this::class.java.simpleName
    private var compositeDisposable: MutableList<Disposable> = mutableListOf()

    override fun addCompositeDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun finishDisposable() {
        for (disposable in compositeDisposable) try {
            disposable.dispose()
        } catch (e: Exception) {
            Log.debug(mTagClass, "error: finishDisposable: ${e.message}")
        }
    }

    fun onDestroy() { finishDisposable() }
}