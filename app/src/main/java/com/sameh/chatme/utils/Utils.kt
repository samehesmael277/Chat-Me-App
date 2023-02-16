package com.sameh.chatme.utils

import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val myFormat = SimpleDateFormat("EEE d, MMM yyyy HH:mm:ss " )
    return myFormat.format(calendar.time)
}