package com.example.myapplication.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(str: String): List<Int> {
        return str.split(",").map { it.toInt() }
    }
}