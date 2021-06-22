package com.movieapp.diditify.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

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