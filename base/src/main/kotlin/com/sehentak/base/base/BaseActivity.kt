package com.sehentak.base.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.app_toolbar_center.*
import com.sehentak.base.BuildConfig
import com.sehentak.base.R
import com.sehentak.base.helper.Log
import java.io.Serializable

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {
    private var mAlertDialog: AlertDialog? = null

    fun AppCompatActivity.setContentView(layoutResID: Int, title: String?) {
        this.setContentView(layoutResID)
        if (title != null) {
            app_toolbar.title = title
            app_toolbar.setTitleTextColor(Color.parseColor("#000000"))
        }
    }

    fun AppCompatActivity.setContentViewCenter(layoutResID: Int, title: String?) {
        this.setContentViewCenter(layoutResID, title, null)
    }

    fun AppCompatActivity.setContentViewCenter(layoutResID: Int, title: String?, isBackVisible: Boolean?) {
        this.setContentView(layoutResID)
        if (title != null) app_toolbar_center_title.text = title
        if (isBackVisible != null && isBackVisible) {
            app_toolbar_center_back.visibility = View.VISIBLE
            app_toolbar_center_back.setOnClickListener { this.finish() }
        }
    }

    fun Context.finishResult(result: Serializable?) {
        val intent = Intent()
        if (result != null) intent.putExtra(getString(R.string.prop_extra_default), result)
        (this as Activity).setResult(if (result != null) Activity.RESULT_OK else Activity.RESULT_CANCELED, intent)
        this.finish()
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

    fun Context.baseIntentForResult(target: Class<*>): Boolean {
        return baseIntentForResult(target, null, null)
    }

    fun Context.baseIntentForResult(target: Class<*>, extra: Serializable?): Boolean {
        return baseIntentForResult(target, extra, null)
    }

    fun Context.baseIntentForResult(
        target: Class<*>,
        extra: Serializable?,
        flags: Int?
    ): Boolean {
        val intent = Intent(this, target)
        if (extra != null) intent.putExtra(getString(R.string.prop_extra_default), extra)
        if (flags != null) intent.flags = flags

        val code = resources.getInteger(R.integer.prop_default_code)
        (this as Activity).startActivityForResult(intent, code)
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
            Toast.makeText(this, message.toString().trim(), Toast.LENGTH_LONG).show()
        }
    }

    fun Context.baseToast(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        if (BuildConfig.DEBUG) {
            Log.debug(this::class.java.simpleName, message)
        }
    }
}