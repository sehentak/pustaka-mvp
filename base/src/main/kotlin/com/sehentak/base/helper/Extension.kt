package com.sehentak.base.helper

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.Html
import android.view.View
import android.widget.TextView
import java.io.File
import java.lang.reflect.Method
import java.text.NumberFormat
import java.util.*

@Suppress("DEPRECATION")
fun Context.uriToImageFile(uri: Uri): File? {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, filePathColumn, null, null, null)
    if (cursor != null) {
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            return File(filePath)
        }
        cursor.close()
    }
    return null
}

@Suppress("DEPRECATION")
fun Context.uriToBitmap(uri: Uri): Bitmap {
    return MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
}

@Suppress("DEPRECATION")
fun TextView.setTextHtml(htmlString: String?) {
    if (text != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
        } else this.text = Html.fromHtml(htmlString)
    }
}

fun Double.toCurrencyFormat(): String {
    return this.toLong().toCurrencyFormat()
}

fun Int.toCurrencyFormat(): String {
    return this.toLong().toCurrencyFormat()
}

fun Long.toCurrencyFormat(): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.GERMANY)
    var currency: String = format.format(this.toDouble())
    if (currency.contains("€")) currency = currency.replace("€", "")
    if (currency.contains(",")) currency = currency.split(",")[0]
    return currency.trim()
}

fun Context.dp(sizeInDp: Int): Int {
    val scale = resources.displayMetrics.density
    return (sizeInDp * scale + 0.5f).toInt()
}

fun Context.dp(sizeInDp: Double): Int {
    val scale = resources.displayMetrics.density
    return (sizeInDp * scale + 0.5f).toInt()
}

fun randomString(): String {
    return randomString(24)
}

fun randomString(header: String): String {
    return if (header.length < 24) "${header}_${randomString(23 - header.length)}"
    else header
}

fun randomString(header: String, length: Int): String {
    val size = header.length
    return if (length > size) {
        "$header${randomString(length - size)}"
    } else header
}

fun randomString(length: Int): String {
    val saltChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"
    val salt = StringBuilder()
    val rnd = Random()
    while (salt.length < length) { // length of the random string.
        val index = (rnd.nextFloat() * saltChar.length).toInt()
        salt.append(saltChar[index])
    }
    return salt.toString()
}

fun View.currentHeight(): Int {
    var result = 0
    this.postDelayed({
        this.invalidate()
        result = this.height
    }, 1000)
    return result
}

@SuppressLint("PrivateApi", "HardwareIds")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
fun getSerialNumber(): String? {
    var serialNumber: String?
    try {
        val c = Class.forName("android.os.SystemProperties")
        val get: Method = c.getMethod("get", String::class.java)
        serialNumber = get.invoke(c, "gsm.sn1").toString()
        if (serialNumber == "") serialNumber = get.invoke(c, "ril.serialnumber").toString()
        if (serialNumber == "") serialNumber = get.invoke(c, "ro.serialno").toString()
        if (serialNumber == "") serialNumber = get.invoke(c, "sys.serialnumber").toString()
        if (serialNumber == "") serialNumber = Build.SERIAL

        // If none of the methods above worked
        if (serialNumber == "") serialNumber = null
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
        serialNumber = null
    }
    return serialNumber
}

@Suppress("DEPRECATION")
fun Context.isMyServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}