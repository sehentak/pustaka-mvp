package com.sehentak.base.base

import com.sehentak.base.BuildConfig
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException

/**
 * An abstract {@link Observer} that allows asynchronous cancellation by implementing Disposable.
 *
 * <p>All pre-implemented final methods are thread-safe.
 *
 * <p>Use the public {@link #dispose()} method to dispose the sequence from within an
 * {@code onNext} implementation.
 *
 * <p>Like all other consumers, {@code DisposableObserver} can be subscribed only once.
 * Any subsequent attempt to subscribe it to a new source will yield an
 * {@link IllegalStateException} with message {@code "It is not allowed to subscribe with a(n) <class name> multiple times."}.
 *
 * <p>Implementation of {@link #onStart()}, {@link #onNext(Object)}, {@link #onError(Throwable)}
 * and {@link #onComplete()} are not allowed to throw any unchecked exceptions.
 * If for some reason this can't be avoided, use {@link io.reactivex.Observable#safeSubscribe(io.reactivex.Observer)}
 * instead of the standard {@code subscribe()} method.
 *
 * @author Angger Prasetyo - angger@sehentak.com
 * @since 2019-08-07
 */
abstract class BaseObserver<T>: DisposableObserver<T>(){
    private val mTagClass = this::class.java.simpleName

    /**
     * Custom response Error
     * To determine and bring errorCode and error message
     * @param errorCode error code from HTTP status
     * @param errorMessage error message from HTTP body message
     */
    abstract fun onResponseError(errorCode: Int, errorMessage: String?)

    /**
     * Custom response Success
     * To determine and bring result with andy data into
     * @param result the item emitted by the Observable
     */
    abstract fun onResponseSuccess(result: T)

    /**
     * Emitted result
     * When the observable already completed
     */
    override fun onComplete() { }

    /**
     * Provides the Observer with a new item to observe.
     * The [Observable] may call this method 0 or more times.
     * Call this method again after it calls either [Observable.doOnComplete]
     *
     * @param t the item emitted by the Observable
     */
    override fun onNext(t: T) {
        onResponseSuccess(t)
    }

    /**
     * Rethrows the throwable if it is a fatal exception or calls {@link #onError(Throwable)}.
     * @param e the throwable to rethrow or signal to the actual subscriber
     */
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