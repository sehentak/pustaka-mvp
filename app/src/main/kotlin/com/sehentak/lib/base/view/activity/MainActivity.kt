package com.sehentak.lib.base.view.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sehentak.base.BuildConfig.*
import com.sehentak.base.base.BaseActivity
import com.sehentak.base.helper.Log
import com.sehentak.lib.base.R
import com.sehentak.lib.base.model.VersionMdl
import com.sehentak.lib.base.view.adapter.HistoryAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: BaseActivity(), MainView {
    private val mTagClass = this::class.java.simpleName
    private lateinit var instancePresenter: MainPresenter
    private lateinit var instanceAdapter: HistoryAdapter
    private val mList: MutableList<VersionMdl> = mutableListOf()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mPresenter.callApiToGetReleaseHistory()
    }

    override fun onSuccessGetReleaseHistory(result: List<VersionMdl>) {
        mList.clear()

        main_lv_history.visibility = View.VISIBLE
        main_tv_empty.visibility = View.GONE
        if (result.isNotEmpty()) mList.addAll(result)
        else mList.add(localVersion)

        mAdapter.notifyDataSetChanged()
    }

    override fun loadingStart(tag: String) {
        main_tv_empty.visibility = View.GONE
        baseLoading(true)
    }

    override fun loadingStop(tag: String) {
        baseLoading(false)
    }

    override fun loadingError(tag: String, errorCode: Int, errorMessage: String?) {
        Log.debug(mTagClass, "error: $tag: $errorCode: $errorMessage")
        onSuccessGetReleaseHistory(arrayListOf())
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.finishDisposable()
    }

    private val localVersion: VersionMdl get() {
        return VersionMdl(
            name = VERSION_NAME,
            code = VERSION_CODE,
            author = KEY_PACKAGE,
            date = RELEASE_DATE
        )
    }

    private val mAdapter: HistoryAdapter get() {
        if (!::instanceAdapter.isInitialized) {
            instanceAdapter = HistoryAdapter(mList)
            main_lv_history.layoutManager = LinearLayoutManager(this)
            main_lv_history.adapter = instanceAdapter
        }
        return instanceAdapter
    }
    
    private val mPresenter: MainPresenter get() {
        if (!::instancePresenter.isInitialized) {
            instancePresenter = MainPresenter(this)
        }
        return instancePresenter
    }
}