package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Report::class, Outlet::class, Goods::class], version = 3)
abstract class RoomDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
    abstract fun outletDao(): OutletDao
    abstract fun goodsDao(): GoodsDao

    companion object {
        @Volatile
        private var instance: RoomDatabase? = null

        fun getInstance(context: Context): RoomDatabase {
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "app_database"
                ).build()
                instance = db
                db
            }
        }
    }
}
