package com.example.nikhil.unsplashimagesearch.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        public fun stringToDate(date:String, datePattern:String): Date {
            val format = SimpleDateFormat(datePattern, Locale.ENGLISH)
            try {
                return format.parse(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return Date()
        }

    }
}