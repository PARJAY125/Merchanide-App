package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outlet")
data class Outlet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val outletName: String,
    val outletLocation: String
)