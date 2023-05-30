package com.example.myapplication.util

import java.text.SimpleDateFormat
import java.util.*

class TimeTools {

    companion object {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        fun getCurrentTime(): String {
            return sdf.format(Date())
        }
    }

}