package com.sehentak.lib.base.view.activity

import com.sehentak.base.base.BaseObserver
import com.sehentak.base.base.BasePresenter
import com.sehentak.lib.base.model.VersionMdl
import com.sehentak.lib.base.repo.RepoImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainPresenter(view: MainView): BasePresenter<MainView>(view) {
    private val mTagClass = this::class.java.simpleName
    private lateinit var instanceRepo: RepoImpl

    val tagApiToGetReleaseHistory = "$mTagClass.gr"

    fun callApiToGetReleaseHistory() {
        view.loadingStart(tagApiToGetReleaseHistory)
        addCompositeDisposable(mRepo
            .getReleaseHistory()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeWith(object : BaseObserver<List<VersionMdl>>(){
                override fun onResponseError(errorCode: Int, errorMessage: String?) {
                    view.loadingStop(tagApiToGetReleaseHistory)
                    view.loadingError(tagApiToGetReleaseHistory, errorCode, errorMessage)
                }

                override fun onResponseSuccess(result: List<VersionMdl>) {
                    view.loadingStop(tagApiToGetReleaseHistory)
                    view.onSuccessGetReleaseHistory(result)
                }
            })
        )
    }

    private val mRepo: RepoImpl get() {
        if (!::instanceRepo.isInitialized) {
            instanceRepo = RepoImpl()
        }
        return instanceRepo
    }
}