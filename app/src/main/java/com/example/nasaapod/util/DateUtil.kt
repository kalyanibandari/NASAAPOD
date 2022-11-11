package com.example.nasaapod.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    companion object {
        private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        /**
         * This method returns today's date in "yyyy-MM-dd" format
         */
        fun todayDate(): String {
            val calendar = Calendar.getInstance(Locale.US)
            return simpleDateFormat.format(calendar.time)
        }

        /**
         * This method converts date format to "yyyy-MM-dd" format
         */
        fun formatTheDate(dateToFormat : String): String{
            val formattedDate: Date = simpleDateFormat.parse(dateToFormat)!!
            return simpleDateFormat.format(formattedDate)
        }

    }
}