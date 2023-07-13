package com.example.myapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "reports")
@TypeConverters(Converters::class)
data class Report(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "outlet_name")
    var outletName: String,

    // image String URI
    @ColumnInfo(name = "transport_distance")
    var transportDistance: String?,
    @ColumnInfo(name = "image_pap_outlet")
    var imagePapOutlet: String?,

    @ColumnInfo(name = "list_goods_sold")
    var listGoodsSold: List<Int>,

    @ColumnInfo(name = "start_time")
    val startTime: String,
    @ColumnInfo(name = "end_time")
    val endTime: String
)