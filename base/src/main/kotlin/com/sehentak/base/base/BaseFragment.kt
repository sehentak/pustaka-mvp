package com.sehentak.base.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.sehentak.base.R
import com.sehentak.base.helper.Log
import java.io.Serializable

abstract class BaseFragment: Fragment() {
    private val mTagClass = this::class.java.simpleName
    private var mAlertDialog: AlertDialog? = null
    private var mSavedInstanceState: Bundle? = null
    private var hasInflated = false
    private var mViewStub: ViewStub? = null

    @Suppress("DEPRECATION")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.app_fragment, container, false)
        mViewStub = view.findViewById(R.id.fragment_viewstub) as ViewStub
        mViewStub!!.layoutResource = getViewStubLayoutResource()
        mSavedInstanceState = savedInstanceState

        if (userVisibleHint && !hasInflated) {
            val inflatedView = mViewStub!!.inflate()
            onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState)
            afterViewStubInflated(view)
        }

        return view
    }

    protected abstract fun onCreateViewAfterViewStubInflated(inflatedView: View, savedInstanceState: Bundle?)

    /**
     * The layout ID associated with this ViewStub
     * @see ViewStub.setLayoutResource
     * @return
     */
    @LayoutRes
    protected abstract fun getViewStubLayoutResource(): Int

    /**
     *
     * @param originalViewContainerWithViewStub
     */
    @CallSuper
    protected fun afterViewStubInflated(originalViewContainerWithViewStub: View?) {
        hasInflated = true
        if (originalViewContainerWithViewStub != null) {
            val pb = originalViewContainerWithViewStub.findViewById<ProgressBar>(R.id.fragment_progress)
            pb.visibility = View.GONE
        }
    }

    @Suppress("DEPRECATION")
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && mViewStub != null && !hasInflated) {
            try {
                val inflatedView = mViewStub?.inflate()
                if (inflatedView != null) onCreateViewAfterViewStubInflated(inflatedView, mSavedInstanceState)
                afterViewStubInflated(view)
            } catch (e: Exception) {
                Log.debug(mTagClass, e.message)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        hasInflated = false
    }

    fun Context.finishResult(result: Serializable?) {
        val intent = Intent()
        if (result != null) intent.putExtra(getString(R.string.prop_extra_default), result)
        (this as Activity).setResult(if (result != null) Activity.RESULT_OK else Activity.RESULT_CANCELED, intent)
        this.finish()
    }

    fun Context.baseIntentResult(target: Class<*>) {
        val intent = Intent(this, target)
        (this as Activity).startActivityForResult(intent, resources.getInteger(R.integer.prop_extra_code))
    }

    fun Context.baseIntent(target: Class<*>): Boolean {
        return this.baseIntent(target, null, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun Context.baseIntent(target: Class<*>, isFinish: Boolean): Boolean {
        return this.baseIntent(target, null, isFinish, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun Context.baseIntent(target: Class<*>, serializable: Serializable): Boolean {
        return this.baseIntent(target, serializable, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun Context.baseIntent(target: Class<*>, serializable: Serializable, isFinish: Boolean): Boolean {
        return this.baseIntent(target, serializable, isFinish, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun Context.baseIntent(
        target: Class<*>,
        extra: Serializable? /* default is null */,
        isFinish: Boolean /* default is false */,
        flags: Int? /* default is null */
    ): Boolean {

        val intent = Intent(this, target)
        if (extra != null) intent.putExtra(getString(R.string.prop_extra_default), extra)
        if (flags != null) intent.flags = flags
        (this as Activity).startActivity(intent)
        if (isFinish) this.finish()
        return true
    }

    fun Context.baseIntent(target: Class<*>, extra: String?): Boolean {
        return this.baseIntent(target, extra, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun Context.baseIntent(
        target: Class<*>,
        extra: String? /* default is null */,
        isFinish: Boolean /* default is false */,
        flags: Int? /* default is null */
    ): Boolean {

        val intent = Intent(this, target)
        if (extra != null) intent.putExtra(getString(R.string.prop_extra_default), extra)
        if (flags != null) intent.flags = flags
        (this as Activity).startActivity(intent)
        if (isFinish) this.finish()
        return true
    }

    fun Context.baseIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun Context.baseLoading(mode: Boolean) {
        this.baseLoading(R.layout.app_loading, mode)
    }

    @SuppressLint("InflateParams")
    fun Context.baseLoading(resourceLayout: Int, mode: Boolean) {
        this.baseLoading(layoutInflater.inflate(resourceLayout, null), mode)
    }

    @Suppress("DEPRECATION")
    fun Context.baseLoading(view: View, mode: Boolean) {
        if (mode) {
            val dialogBuilder = AlertDialog.Builder(this, R.style.AppTheme_Dialog)
            dialogBuilder.setView(view)

            mAlertDialog = dialogBuilder.create()
            mAlertDialog?.setCanceledOnTouchOutside(false)
            mAlertDialog?.setCancelable(false)
            mAlertDialog?.show()
        } else {
            if (mAlertDialog != null) {
                mAlertDialog?.hide()
                mAlertDialog?.dismiss()
                mAlertDialog = null
            }
        }
    }

    fun Context.baseToast(message: Any?) {
        if (message != null) {
            Toast.makeText(this, message.toString().trim(), Toast.LENGTH_SHORT).show()
        }
    }

    fun Context.baseToast(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}