@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.movieapp.diditify.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    @SuppressLint("SimpleDateFormat")
    fun isCurrentYear(releaseDate: String): Boolean {
        return try {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val format = SimpleDateFormat(CALENDAR_PATTERN)
            val date = format.parse(releaseDate)
            val df = SimpleDateFormat(CALENDAR_YEAR_PATTERN)
            val mYear = df.format(date)
            return mYear == year.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}