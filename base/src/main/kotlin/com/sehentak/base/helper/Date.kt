package com.sehentak.base.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.sehentak.base.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import java.util.Date

/**
 * Created by angger on 2019-08-07.
 */

object Date {
    private val mTagClass = this::class.java.simpleName

    const val pattern: String = "yyyy-MM-dd HH:mm:ss"
    const val patternAPI = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
    const val patternTime: String = "HH:mm"
    const val patternDate: String = "yyyy-MM-dd"
    const val patternGlobal = "E, dd MMM yyyy kk:mm:ss"
    const val patternDay: String = "EEEE, dd MMM yyyy"
    const val patternLocal: String = "dd-MM-yyyy HH:mm"
    const val patternLocalDate: String = "dd-MM-yyyy"
    const val patternLocalDateTime: String = "dd-MM-yyyy HH:mm:ss"

    fun convertDateToLong(date: String): Long {
        return convertDateToLong(date, pattern)
    }

    fun convertDateGMT(timestamp: Long?): String? {
        return convertDateGMT(timestamp, pattern)
    }

    fun convertDateGMT(timestamp: Long?, pattern: String): String? {
        return if (timestamp != null) {
            val date = Date(timestamp)
            convertDateGMT(date, pattern)
        } else null
    }

    fun convertDateGMT(date: String): String? {
        return convertDateGMT(date, pattern)
    }

    @Suppress("DEPRECATION")
    fun convertDateGMT(date: String, pattern: String): String? {
        return try {
            val original = toDate(date, pattern)
            if (original != null) convertDateGMT(original, pattern)
            else null
        } catch (e: Exception) {
            Log.debug("convertDateGMT", e)
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateGMT(date: Date, pattern: String): String? {
        val dateFormatGmt = SimpleDateFormat(pattern)
//        val reqHoursInMillis = 7 * 60 * 60 * 1000
        val resultDate = Date(date.time /*+ reqHoursInMillis*/)
        return dateFormatGmt.format(resultDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun toDate(date: String, pattern: String): Date? {
        var mDate = date
        if (mDate.contains("T")) mDate = mDate.replace("T", " ")
        if (mDate.contains(".")) mDate = mDate.split(".")[0]

        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.parse(mDate)
    }

    @SuppressLint("SimpleDateFormat")
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun convertDateToLong(date: String, pattern: String): Long {
        return toDate(date, pattern)?.time ?: 0
    }

    fun millis(): Long {
        return millis(false)
    }

    fun millis(isUsed1000: Boolean): Long {
        var result = System.currentTimeMillis()
        if (isUsed1000) result %= 1000
        return result
    }

    fun toDate(timeStamp: Long): Date{
        java.util.Date().time
        return Date(timeStamp)
    }

    fun getDate(): String {
        return getDate(Date(), pattern)
    }

    fun getDate(date: Date): String {
        return getDate(date, pattern)
    }

    fun getDate(pattern: String): String {
        return getDate(Date(), pattern)
    }

    @SuppressLint("SimpleDateFormat")
    fun getDate(date: Date, pattern: String): String{
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }

    fun generateDayFormat(context: Context?): String {
        return generateDayFormat(context, Date())
    }

    @SuppressLint("SimpleDateFormat")
    fun generateDayFormat(context: Context?, date: Date): String {
        try {
            val dateFormat = SimpleDateFormat(patternDay)
            val result = dateFormat.format(date)
            val dayEnglish = context?.resources?.getStringArray(R.array.day_english)
            val dayIndonesia = context?.resources?.getStringArray(R.array.day_indonesian)

            val language = context?.getString(R.string.label_language)
            val currentDate = result.split(",")[1].trim()
            return if (dayEnglish != null && dayIndonesia != null && language == "bahasa")
                when(result.split(", ")[0]) {
                    dayEnglish[0] -> "${dayIndonesia[0]}, $currentDate"
                    dayEnglish[1] -> "${dayIndonesia[1]}, $currentDate"
                    dayEnglish[2] -> "${dayIndonesia[2]}, $currentDate"
                    dayEnglish[3] -> "${dayIndonesia[3]}, $currentDate"
                    dayEnglish[4] -> "${dayIndonesia[4]}, $currentDate"
                    dayEnglish[5] -> "${dayIndonesia[5]}, $currentDate"
                    dayEnglish[6] -> "${dayIndonesia[6]}, $currentDate"
                    else -> result
            } else if (dayEnglish != null && dayIndonesia != null && language == "english")
                when (result.split(", ")[0]) {
                    dayEnglish[0] -> "${dayEnglish[0]}, $currentDate"
                    dayEnglish[1] -> "${dayEnglish[1]}, $currentDate"
                    dayEnglish[2] -> "${dayEnglish[2]}, $currentDate"
                    dayEnglish[3] -> "${dayEnglish[3]}, $currentDate"
                    dayEnglish[4] -> "${dayEnglish[4]}, $currentDate"
                    dayEnglish[5] -> "${dayEnglish[5]}, $currentDate"
                    dayEnglish[6] -> "${dayEnglish[6]}, $currentDate"
                    else -> result
            } else result
        } catch (e: Exception) {
            Log.debug(mTagClass, "generateDayFormat: ${e.message}")
            return ""
        }
    }

    fun isElapsed(startTime: String, endTime: String): Boolean {
        return isElapsed(startTime, endTime, patternLocal, patternLocalDate)
    }

    fun isElapsed(startTime: String, endTime: String, patternFull: String, patternDate: String): Boolean {
        val date = getDate(patternFull)
        val dateNow = getDate(patternDate)
        val a = convertDateToLong(
            if (startTime.contains(" ")) startTime else "$dateNow $startTime",
            patternFull
        )
        val b = convertDateToLong(
            if (startTime.contains(" ")) endTime else "$dateNow $endTime",
            patternFull
        )
        val now = convertDateToLong(date, patternFull)
        return now in a until b
    }

    fun calculateRemaining(endTime: String): Long {
        return calculateRemaining(endTime, pattern)
    }

    fun calculateRemaining(endTime: String, pattern: String): Long {
        return calculateRemaining(getDate(), endTime, pattern)
    }

    fun calculateRemaining(startTime: String, endTime: String, pattern: String): Long {
        val dateNow = getDate(pattern)
        val date = getDate(pattern)
        val a = convertDateToLong(
            if (startTime.contains(" ")) startTime else "$dateNow $startTime",
            pattern
        )
        val b = convertDateToLong(if (endTime.contains(" ")) endTime else "$date $endTime", pattern)
        return b - a
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentWeek(): Int {
        val date = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        return date.get(weekFields.weekOfWeekBasedYear())
    }
}