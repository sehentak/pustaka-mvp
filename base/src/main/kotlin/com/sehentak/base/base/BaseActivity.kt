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
import com.sehentak.base.BuildConfig
import com.sehentak.base.R
import com.sehentak.base.helper.Log
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.app_toolbar_center.*
import java.io.Serializable

/**
 * Base class for activities that wish to use some of the newer platform features on older
 * Android devices. Some of these backported features include:
 *
 * <ul>
 *     <li>Using the action bar, including action items, navigation modes and more with
 *     the {@link #setSupportActionBar(Toolbar)} API.</li>
 *     <li>Built-in switching between light and dark themes by using the
 *     {@link androidx.appcompat.R.style#Theme_AppCompat_DayNight Theme.AppCompat.DayNight} theme
 *     and {@link AppCompatDelegate#setDefaultNightMode(int)} API.</li>
 *     <li>Integration with <code>DrawerLayout</code> by using the
 *     {@link #getDrawerToggleDelegate()} API.</li>
 * </ul>
 *
 * <p>Note that every activity that extends this class has to be themed with
 * {@link androidx.appcompat.R.style#Theme_AppCompat Theme.AppCompat} or a theme that extends
 * that theme.</p>
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 *
 * <p>For information about how to use the action bar, including how to add action items, navigation
 * modes and more, read the <a href="{@docRoot}guide/topics/ui/actionbar.html">Action
 * Bar</a> API guide.</p>
 * </div>
 */
@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {
    private var mAlertDialog: AlertDialog? = null

    /**
     * Should be called instead of [Activity.setContentView]
     * @param layoutResID Layout resource reference
     * @param title Title to set
     */
    fun AppCompatActivity.setContentView(layoutResID: Int, title: String?) {
        this.setContentView(layoutResID)
        if (title != null) {
            app_toolbar.title = title
            app_toolbar.setTitleTextColor(Color.parseColor("#000000"))
        }
    }

    /**
     * Custom setContentView with center title on toolbar set
     * @param layoutResID Layout resource reference
     * @param title Title to set
     */
    fun AppCompatActivity.setContentViewCenter(layoutResID: Int, title: String?) {
        this.setContentViewCenter(layoutResID, title, null)
    }

    /**
     * Custom setContentView with center title on toolbar set
     * @param layoutResID Layout resource reference
     * @param title Title to set
     * @param isBackVisible back button visibility
     */
    fun AppCompatActivity.setContentViewCenter(layoutResID: Int, title: String?, isBackVisible: Boolean?) {
        this.setContentView(layoutResID)
        if (title != null) app_toolbar_center_title.text = title
        if (isBackVisible != null && isBackVisible) {
            app_toolbar_center_back.visibility = View.VISIBLE
            app_toolbar_center_back.setOnClickListener { this.finish() }
        }
    }

    /**
     * Finish activity with intent result
     * @param result extra data with [Serializable] file support
     */
    fun Context.finishResult(result: Serializable?) {
        val intent = Intent()
        if (result != null) intent.putExtra(getString(R.string.prop_extra_default), result)
        (this as Activity).setResult(if (result != null) Activity.RESULT_OK else Activity.RESULT_CANCELED, intent)
        this.finish()
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @return [Boolean.true]
     */
    fun Context.baseIntent(target: Class<*>): Boolean {
        return this.baseIntent(target, null, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param isFinish is activity finish
     * @return [Boolean.true]
     */
    fun Context.baseIntent(target: Class<*>, isFinish: Boolean): Boolean {
        return this.baseIntent(target, null, isFinish, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param serializable extra data
     * @return [Boolean.true]
     */
    fun Context.baseIntent(target: Class<*>, serializable: Serializable): Boolean {
        return this.baseIntent(target, serializable, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param serializable extra data
     * @param isFinish is activity finish
     * @return [Boolean.true]
     */
    fun Context.baseIntent(target: Class<*>, serializable: Serializable, isFinish: Boolean): Boolean {
        return this.baseIntent(target, serializable, isFinish, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param extra extra data
     * @param isFinish is activity finish
     * @param flags with default is [Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * @return [Boolean.true]
     */
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

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param extra extra string
     * @return [Boolean.true]
     */
    fun Context.baseIntent(target: Class<*>, extra: String?): Boolean {
        return this.baseIntent(target, extra, false, Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    /**
     * Intent to target class
     * Simple shortcut for use this intent
     * @param target target class
     * @param extra extra data
     * @param isFinish is activity finish
     * @param flags with default is [Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * @return [Boolean.true]
     */
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

    /**
     * Intent to target class
     * implementation with [Activity.startActivityForResult]
     * @param target target class
     * @return [Boolean.true]
     */
    fun Context.baseIntentForResult(target: Class<*>): Boolean {
        return baseIntentForResult(target, null, null)
    }

    /**
     * Intent to target class
     * implementation with [Activity.startActivityForResult]
     * @param target target class
     * @param extra extra data
     * @return [Boolean.true]
     */
    fun Context.baseIntentForResult(target: Class<*>, extra: Serializable?): Boolean {
        return baseIntentForResult(target, extra, null)
    }

    /**
     * Intent to target class
     * implementation with [Activity.startActivityForResult]
     * @param target target class
     * @param extra extra data
     * @param flags with default is [Intent.FLAG_ACTIVITY_SINGLE_TOP]
     * @return [Boolean.true]
     */
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

    /**
     * Special intent to call webView / browser
     * @param url endpoint to load the page
     */
    fun Context.baseIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    /**
     * Dialog base with circle progress
     * @param mode when is true, dialog is show up
     */
    fun Context.baseLoading(mode: Boolean) {
        this.baseLoading(R.layout.app_loading, mode)
    }

    /**
     * Dialog base with circle progress
     * Sets a custom view to be the contents of the alert dialog.
     *
     * @param layoutResID custom Layout resource reference
     * @param mode when is true, dialog is show up
     */
    @SuppressLint("InflateParams")
    fun Context.baseLoading(layoutResID: Int, mode: Boolean) {
        this.baseLoading(layoutInflater.inflate(layoutResID, null), mode)
    }

    /**
     * Dialog base with circle progress
     * Sets a custom view to be the contents of the alert dialog.
     *
     * @param view the view to use as the contents of the alert dialog
     * @param mode when is true, dialog is show up
     */
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

    /**
     * Simple method for displaying [Toast]
     * @param message
     */
    fun Context.baseToast(message: Any?) {
        if (message != null) {
            Toast.makeText(this, message.toString().trim(), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Simple method for displaying [Toast]
     * @param message
     */
    fun Context.baseToast(message: String?) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        if (BuildConfig.DEBUG) {
            Log.debug(this::class.java.simpleName, message)
        }
    }
}