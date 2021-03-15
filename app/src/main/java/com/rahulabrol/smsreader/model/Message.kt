package com.rahulabrol.smsreader.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rahul Abrol on 14/3/21.
 */

abstract class ListItem {
    val TYPE_DATE = 0
    val TYPE_GENERAL = 1
    abstract fun getType(): Int
}

@Parcelize
class GeneralItem(
    var _address: String,
    var _msg: String,
    var _time: String = ""
) : ListItem(), Parcelable {

    override fun getType(): Int {
        return TYPE_GENERAL
    }
}

class DateItem(
    var date: String
) : ListItem() {

    override fun getType(): Int {
        return TYPE_DATE
    }

}

@Parcelize
data class Message(
    var _address: String,
    var _msg: String,
    var _time: String
) : Parcelable