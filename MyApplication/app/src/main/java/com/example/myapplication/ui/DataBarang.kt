package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.database.Goods
import com.example.myapplication.dialog.DialogTwoInput
import com.example.myapplication.recViewAdapter.DataBarangAdapter
import com.example.myapplication.recViewAdapter.RecyclerItemClickListener
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_data_barang.ADB_fab_add_product
import kotlinx.android.synthetic.main.activity_data_barang.ADB_rv_product_data_list

// TODO : MAY CAUSE FATAL ERROR ganti jadi dialog two input
class DataBarang : AppCompatActivity(), DialogTwoInput.DialogListener {

    private lateinit var dataBarangAdapter: DataBarangAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_barang)

        ADB_fab_add_product.setOnClickListener {
            // TODO : open dialog box -> add data
            val dialogFragment = DialogTwoInput()
            dialogFragment.show(supportFragmentManager, "AddProduct")
        }

        initViewModel()
        setUpRecView()
    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun setUpRecView() {
        ADB_rv_product_data_list.layoutManager = LinearLayoutManager(this)

        dataBarangAdapter = DataBarangAdapter(emptyList())
        ADB_rv_product_data_list.adapter = dataBarangAdapter

        // get data from database
        viewModel.allGoods.observe(this) { dataList ->
            dataBarangAdapter.setData(dataList)
        }

        ADB_rv_product_data_list.addOnItemTouchListener(RecyclerItemClickListener(this) { _, position ->
            // Handle the item click here
            val clickedGoodsItem = dataBarangAdapter.getItemAtPosition(position)

            val args = Bundle()
            args.putParcelable("goodsData", clickedGoodsItem) // Put the Goods object in the Bundle
            val dialogFragment = DialogTwoInput()
            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "EditProduct")
        })
    }

    override fun onDialogResult(namaProduk: String, hargaProduk : Double) {
        viewModel.insertGoods(Goods(0, namaProduk, hargaProduk))
        Toast.makeText(this, "$namaProduk berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogEditResult(goods: Goods) {
        viewModel.updateGoods(goods)
        Toast.makeText(this, "${goods.goodsName} telah diubah", Toast.LENGTH_SHORT).show()
    }
}