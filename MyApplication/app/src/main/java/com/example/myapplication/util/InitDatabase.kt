package com.example.myapplication.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.database.RoomDatabase
import com.example.myapplication.repository.MyRepository
import com.example.myapplication.viewmodel.MyViewModel
import com.example.myapplication.viewmodel.MyViewModelFactory

class InitDatabase {
    companion object {
        private lateinit var viewModel: MyViewModel

        fun initDatabase(contex : AppCompatActivity): MyViewModel {
            val database = Room.databaseBuilder(contex, RoomDatabase::class.java, "my-database").build()
            val outletDao = database.outletDao()
            val goodsDao = database.goodsDao()
            val reportDao = database.reportDao()


            val viewModelFactory = MyViewModelFactory(MyRepository(outletDao, goodsDao, reportDao))
            return ViewModelProvider(contex, viewModelFactory)[MyViewModel::class.java]
        }
    }
}