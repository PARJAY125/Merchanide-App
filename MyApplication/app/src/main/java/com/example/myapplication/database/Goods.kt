package com.example.myapplication.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class Goods(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var goodsName: String,
    var goodsPrice: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(goodsName)
        parcel.writeDouble(goodsPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Goods> {
        override fun createFromParcel(parcel: Parcel): Goods {
            return Goods(parcel)
        }

        override fun newArray(size: Int): Array<Goods?> {
            return arrayOfNulls(size)
        }
    }
}