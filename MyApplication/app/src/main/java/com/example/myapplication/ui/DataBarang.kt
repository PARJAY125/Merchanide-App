package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.dialog.DialogOneInput
import com.example.myapplication.recViewAdapter.DataBarangAdapter
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_data_barang.ADB_fab_add_product
import kotlinx.android.synthetic.main.activity_data_barang.ADB_rv_outlet_data_list

// TODO : MAY CAUSE FATAL ERROR ganti jadi dialog two input
class DataBarang : AppCompatActivity(), DialogOneInput.DialogListener {

    private lateinit var dataBarangAdapter: DataBarangAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_barang)

        ADB_fab_add_product.setOnClickListener {
            // TODO : open dialog box -> add data
            val dialogFragment = DialogOneInput()
            dialogFragment.show(supportFragmentManager, "AddOutlet")
        }

        initViewModel()
        setUpRecView()
    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun setUpRecView() {
        ADB_rv_outlet_data_list.layoutManager = LinearLayoutManager(this)

        dataBarangAdapter = DataBarangAdapter(emptyList())
        ADB_rv_outlet_data_list.adapter = dataBarangAdapter

        // get data from database
        viewModel.allGoods.observe(this) { dataList ->
            dataBarangAdapter.setData(dataList)
        }
    }

    override fun onDialogResult(value: String) {
        // Handle the value received from the DialogFragment
        // Perform any necessary actions or update UI
        Toast.makeText(this, value, Toast.LENGTH_SHORT).show()
    }
}