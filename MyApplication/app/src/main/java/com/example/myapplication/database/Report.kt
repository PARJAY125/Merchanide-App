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
    @ColumnInfo(name = "transport_distance")
    var transportDistance: String?,

    // image String URI
    @ColumnInfo(name = "image_before")
    var imageBefore: String?,
    @ColumnInfo(name = "image_after")
    var imageAfter: String?,

    @ColumnInfo(name = "is_stock_full")
    var isStockFull: Boolean,
    @ColumnInfo(name = "list_good_ids")
    var listGoodsIds: List<Int>,

    // todo : make a start time and end time
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @ColumnInfo(name = "end_time")
    val endTime: String
)