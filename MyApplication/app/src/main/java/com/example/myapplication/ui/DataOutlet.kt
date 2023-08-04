package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.database.Outlet
import com.example.myapplication.dialog.DialogOneInput
import com.example.myapplication.dialog.DialogTwoInput
import com.example.myapplication.recViewAdapter.DataOutletAdapter
import com.example.myapplication.recViewAdapter.RecyclerItemClickListener
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_data_outlet.ADO_fab_add_outlet
import kotlinx.android.synthetic.main.activity_data_outlet.ADO_rv_outlet_data_list

class DataOutlet : AppCompatActivity(), DialogOneInput.DialogListener {

    private lateinit var dataOutletAdapter: DataOutletAdapter
    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_outlet)

        ADO_fab_add_outlet.setOnClickListener {
            // TODO : open dialog box -> add data
            val dialogFragment = DialogOneInput()
            dialogFragment.show(supportFragmentManager, "OutletCRUD")
        }

        initViewModel()
        setUpRecView()
    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun setUpRecView() {
        ADO_rv_outlet_data_list.layoutManager = LinearLayoutManager(this)

        dataOutletAdapter = DataOutletAdapter(emptyList())
        ADO_rv_outlet_data_list.adapter = dataOutletAdapter

        // get data from database
        viewModel.allOutlets.observe(this) { dataList ->
            dataOutletAdapter.setData(dataList)
        }

        ADO_rv_outlet_data_list.addOnItemTouchListener(RecyclerItemClickListener(this) { _, position ->
            // Handle the item click here
            val clickedGoodsItem = dataOutletAdapter.getItemAtPosition(position)

            val args = Bundle()
            args.putParcelable("outletData", clickedGoodsItem) // Put the Goods object in the Bundle
            val dialogFragment = DialogOneInput()
            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "OutletCRUD")
        })
    }

    override fun onDialogResult(newOutletName: String) {
        viewModel.insertOutlet(Outlet(0, newOutletName))
        Toast.makeText(this, "$newOutletName berhasil ditambahkan", Toast.LENGTH_SHORT).show()
    }

    override fun onEditDialogResult(outlet: Outlet) {
        viewModel.updateOutlet(outlet)
        Toast.makeText(this, "${outlet.outletName} berhasil dirubah", Toast.LENGTH_SHORT).show()
    }
}