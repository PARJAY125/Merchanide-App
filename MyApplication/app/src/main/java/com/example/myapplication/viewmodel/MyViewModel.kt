package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.Goods
import com.example.myapplication.database.Outlet
import com.example.myapplication.database.Report
import com.example.myapplication.repository.MyRepository
import kotlinx.coroutines.launch

class MyViewModel(private val repository: MyRepository) : ViewModel() {

    //  Get all <Live data>
    val allOutlets: LiveData<List<Outlet>> = repository.getAllOutlets       // outlets
    val allGoods: LiveData<List<Goods>> = repository.getAllGoods            // goods
    val allReports: LiveData<List<Report>> = repository.getAllReports       // reports

    // Functions to insert new instances of each data class
    fun insertOutlet(outlet: Outlet) = viewModelScope.launch {
        repository.insertOutlet(outlet)
    }

    fun insertGoods(goods: Goods) = viewModelScope.launch {
        repository.insertGoods(goods)
    }

    fun insertReport(report: Report) = viewModelScope.launch {
        repository.insertReport(report)
    }

    fun updateGoods(goods: Goods) = viewModelScope.launch {
        repository.updateGoods(goods)
    }

    // Functions to delete instances of each data class
    fun deleteOutlet(outlet: Outlet) = viewModelScope.launch {
        repository.deleteOutlet(outlet)
    }

    fun deleteGoods(goods: Goods) = viewModelScope.launch {
        repository.deleteGoods(goods)
    }

    fun deleteReport(report: Report) = viewModelScope.launch {
        repository.deleteReport(report)
    }
}