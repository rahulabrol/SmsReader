package com.rahulabrol.smsreader.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Rahul Abrol on 14/3/21.
 */

object Utils {
    private const val SECOND_MILLIS: Int = 1000
    private const val MINUTE_MILLIS: Int = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS: Int = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS: Int = 24 * HOUR_MILLIS

    private fun currentDate(): Date {
        val calendar: Calendar = Calendar.getInstance()
        return calendar.time
    }

    fun getTimeAgo(d: Date?): String {
        val date: Date = d!!
        var time: Long = date.time
        if (time < 1000000000000L) {
            time *= 1000
        }
        val now: Long = currentDate().time
        if (time > now || time <= 0) {
            return "in the future"
        }
        val diff = now - time
        return when {
            diff < MINUTE_MILLIS -> {
                "0 hour ago"
            }
            diff < 2 * MINUTE_MILLIS -> {
                "0 hour ago"
            }
            diff < 60 * MINUTE_MILLIS -> {
              "1 hour ago"
            }
            diff < 2 * HOUR_MILLIS -> {
                "2 hour ago"
            }
            diff < 3 * HOUR_MILLIS -> {
                "3 hour ago"
            }
            diff < 6 * HOUR_MILLIS -> {
                "6 hour ago"
            }
            diff < 12 * HOUR_MILLIS -> {
                "12 hour ago"
            }
            else -> {
                diff.div(DAY_MILLIS).toString() + " days ago"
            }
        }
    }
}