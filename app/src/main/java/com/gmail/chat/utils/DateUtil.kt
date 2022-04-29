package com.gmail.chat.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("HH:mm MMM d", Locale.getDefault())
        return dateFormat.format(date)
    }
}

