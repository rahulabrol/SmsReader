package com.rahulabrol.smsreader

import android.util.Log
import com.rahulabrol.smsreader.base.LiveCoroutinesViewModel
import com.rahulabrol.smsreader.model.DateItem
import com.rahulabrol.smsreader.model.GeneralItem
import com.rahulabrol.smsreader.model.ListItem
import com.rahulabrol.smsreader.model.Message
import com.rahulabrol.smsreader.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by Rahul Abrol on 14/3/21.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private var homeRepository: HomeRepository) :
    LiveCoroutinesViewModel() {

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    fun getDate(milliSeconds: Long): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun getExtractedList(smsList: MutableList<Message>): ArrayList<ListItem> {
        val groupedHashMap: HashMap<String, List<ListItem>> = LinkedHashMap()
        smsList.forEach {
            val daysAgo = getDifference(it._time)
            val generalItem =
                GeneralItem(it._address, it._msg, getDate(it._time.time))
            if (groupedHashMap.containsKey(daysAgo)) {
                (groupedHashMap[daysAgo] as ArrayList<GeneralItem>).add(generalItem)
            } else {
                val arrayList = arrayListOf<GeneralItem>()
                arrayList.add(generalItem)
                groupedHashMap[daysAgo] = arrayList
            }
        }

        // We linearly add every item into the consolidatedList.
        val consolidatedList = arrayListOf<ListItem>()
        for (date in groupedHashMap.keys) {
            val dateItem = DateItem(date)
            consolidatedList.add(dateItem)
            for (generalItem in groupedHashMap[date]!!) {
                consolidatedList.add(generalItem)
            }
        }
        return consolidatedList
    }

    private fun getDifference(endDate: Date): String {
        val startDate = Date(System.currentTimeMillis())

        val duration: Long = endDate.time - startDate.time

        val diffInHours = TimeUnit.MILLISECONDS.toHours(duration)
        val diffInDays = TimeUnit.MILLISECONDS.toDays(duration)
        var count = ""
        if (diffInDays == 0L) {
            when (Math.abs(diffInHours)) {
                0L -> {
                    count = "0 hours ago"
                }
                1L -> {
                    count = "1 hours ago"
                }
                2L -> {
                    count = "2 hours ago"
                }
                3L -> {
                    count = "3 hours ago"
                }
                4L, 5L, 6L -> {
                    count = "6 hours ago"
                }
                7L, 8L, 9L, 10L, 11L, 12L -> {
                    count = "12 hours ago"
                }
                else -> {
                    count = "1 day ago"
                }
            }
        } else {
            count = "${abs(diffInDays)} days ago"
        }
        return count
    }
}