package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class MyRepository(private val outletDao: OutletDao, private val goodsDao: GoodsDao, private val reportDao: ReportDao) {

    // Get all outlets
    val getAllOutlets: LiveData<List<Outlet>> = outletDao.getAllOutlets()

    // Insert an outlet
    suspend fun insertOutlet(outlet: Outlet) {
        withContext(Dispatchers.IO) {
            outletDao.insert(outlet)
        }
    }

    // Delete an outlet
    suspend fun deleteOutlet(outlet: Outlet) {
        withContext(Dispatchers.IO) {
            outletDao.delete(outlet)
        }
    }

    // Get all goods
    val getAllGoods : LiveData<List<Goods>> = goodsDao.getAllGoods()

    // Insert a goods
    suspend fun insertGoods(goods: Goods) {
        withContext(Dispatchers.IO) {
            goodsDao.insert(goods)
        }
    }

    // Delete a goods
    suspend fun deleteGoods(goods: Goods) {
        withContext(Dispatchers.IO) {
            goodsDao.delete(goods)
        }
    }

    // Get all reports
    val getAllReports : LiveData<List<Report>> = reportDao.getAllReports()

    // Insert a report
    suspend fun insertReport(report: Report) {
        withContext(Dispatchers.IO) {
            reportDao.insert(report)
        }
    }

    // Delete a report
    suspend fun deleteReport(report: Report) {
        withContext(Dispatchers.IO) {
            reportDao.delete(report)
        }
    }
}