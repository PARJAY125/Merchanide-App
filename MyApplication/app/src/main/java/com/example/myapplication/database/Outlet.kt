package com.example.myapplication.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outlet")
data class Outlet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var outletName: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(outletName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Outlet> {
        override fun createFromParcel(parcel: Parcel): Outlet {
            return Outlet(parcel)
        }

        override fun newArray(size: Int): Array<Outlet?> {
            return arrayOfNulls(size)
        }
    }
}