package com.example.myapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class Goods(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val goodsName: String,
    val goodsPrice: Double
)