package com.sehentak.base.base

import io.reactivex.observers.DisposableObserver
import com.sehentak.base.BuildConfig
import retrofit2.HttpException

/**
 * Created by angger on 2019-08-07.
 */

abstract class BaseObserver<T>: DisposableObserver<T>(){
    private val mTagClass = this::class.java.simpleName

    abstract fun onResponseError(errorCode: Int, errorMessage: String?)
    abstract fun onResponseSuccess(result: T)

    override fun onComplete() { }

    override fun onNext(t: T) {
        onResponseSuccess(t)
    }

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) e.stackTrace

        var code = -1
        var message = e.message
        when(e) {
            is HttpException -> {
                code = e.code()
                message = e.message()
            }
        }

        onResponseError(code, message)
    }
}