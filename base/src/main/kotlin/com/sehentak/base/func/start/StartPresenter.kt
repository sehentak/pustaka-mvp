package com.sehentak.base.func.start

import com.sehentak.base.base.BaseObserver
import com.sehentak.base.base.BasePresenter
import com.sehentak.base.model.ApplicationMdl
import com.sehentak.base.model.PlatformMdl
import com.sehentak.base.repo.BaseImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StartPresenter(view: StartView): BasePresenter<StartView>(view) {
    private val mTagClass = this::class.java.simpleName
    @Volatile private lateinit var instanceRepo: BaseImpl

    val tagApiPlatform = "$mTagClass.gp"
    val tagApiChallenge = "$mTagClass.gc"

    fun callApiPlatform() {
        addCompositeDisposable(mRepo
            .getIpPlatform()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : BaseObserver<PlatformMdl>(){
                override fun onResponseError(errorCode: Int, errorMessage: String?) {
                    view.loadingError(tagApiPlatform, errorCode, errorMessage)
                }

                override fun onResponseSuccess(result: PlatformMdl) {
                    view.onSuccessGetPlatform(result)
                }
            })
        )
    }

    fun callApiChallenge(appVersion: String, platform: PlatformMdl?) {
        addCompositeDisposable(mRepo
            .challenge(appVersion, platform)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : BaseObserver<ApplicationMdl>(){
                override fun onResponseError(errorCode: Int, errorMessage: String?) {
                    view.loadingError(tagApiChallenge, errorCode, errorMessage)
                }

                override fun onResponseSuccess(result: ApplicationMdl) {
                    view.onSuccessBeatTheHeart(result)
                }
            })
        )
    }

    fun callApiChallenge(appId: String, appKey: String, appVersion: String, platform: PlatformMdl?) {
        addCompositeDisposable(mRepo
            .challenge(appId, appKey, appVersion, platform)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : BaseObserver<ApplicationMdl>(){
                override fun onResponseError(errorCode: Int, errorMessage: String?) {
                    view.loadingError(tagApiChallenge, errorCode, errorMessage)
                }

                override fun onResponseSuccess(result: ApplicationMdl) {
                    view.onSuccessBeatTheHeart(result)
                }
            })
        )
    }

    private val mRepo: BaseImpl get() {
        if (!::instanceRepo.isInitialized) {
            instanceRepo = BaseImpl()
        }
        return instanceRepo
    }
}