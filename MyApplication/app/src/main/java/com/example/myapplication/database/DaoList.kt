package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(report: Report)

    @Update
    suspend fun update(report: Report)

    @Delete
    suspend fun delete(report: Report)

    @Query("SELECT * FROM reports WHERE id = :id")
    suspend fun getReportById(id: Long): Report

    @Query("SELECT * FROM reports")
    fun getAllReports(): LiveData<List<Report>>
}

@Dao
interface OutletDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(outlet: Outlet)

    @Update
    suspend fun update(outlet: Outlet)

    @Delete
    suspend fun delete(outlet: Outlet)

    @Query("SELECT * FROM outlet WHERE id = :id")
    suspend fun getOutletById(id: Long): Outlet

    @Query("SELECT * FROM outlet")
    fun getAllOutlets(): LiveData<List<Outlet>>
}

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(goods: Goods)

    @Update
    suspend fun update(goods: Goods)

    @Delete
    suspend fun delete(goods: Goods)

    @Query("SELECT * FROM goods WHERE id = :id")
    suspend fun getGoodsById(id: Long): Goods

    @Query("SELECT * FROM goods")
    fun getAllGoods(): LiveData<List<Goods>>
}