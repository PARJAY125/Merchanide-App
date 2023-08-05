package com.example.myapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.database.Report
import com.example.myapplication.recViewAdapter.ReportEditProductSoldAdapter
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_report_edit_detail.ARED_rv_selled_product
import kotlinx.android.synthetic.main.report_detail.rd_btn_submit_report
import kotlinx.android.synthetic.main.report_detail.rd_iv_pap_outlet
import kotlinx.android.synthetic.main.report_detail.rd_iv_transport
import kotlinx.android.synthetic.main.report_detail.rd_spn_outlet_name
import java.io.File
import java.io.IOException

class ReportEditDetail : AppCompatActivity() {

    private lateinit var reportEditProductSoldAdapter: ReportEditProductSoldAdapter
    private lateinit var viewModel: MyViewModel

    private val CAMERA_REQUEST_CODE = 100
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var imageFile: File

    private lateinit var reportEdited: Report

    private var imageMarker = 0
    private lateinit var imagePapOutlet : String
    private lateinit var imageTransport : String

    lateinit var selectedOutlet : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_edit_detail)

        checkIntentExtra()
        initDatabase()
        populateSpinner()
        onClickFunction()

        rd_spn_outlet_name.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedOutlet = parent?.getItemAtPosition(position).toString()
                // Use the selected item as needed
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected, if needed
            }
        }

        setUpRecView()
    }

    private fun checkIntentExtra() {
        reportEdited = intent.getSerializableExtra("reportId") as Report
    }

    private fun setUpRecView() {
        ARED_rv_selled_product.layoutManager = LinearLayoutManager(this)

        reportEditProductSoldAdapter = ReportEditProductSoldAdapter(emptyList(), reportEdited)
        ARED_rv_selled_product.adapter = reportEditProductSoldAdapter

        // get data from database
        viewModel.allGoods.observe(this) { dataList ->
            reportEditProductSoldAdapter.setData(dataList)
        }
    }

    private fun initDatabase() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun populateSpinner() {
        viewModel.allOutlets.observe(this) { goodsList ->
            val adapter =
                ArrayAdapter(this, android.R.layout.simple_spinner_item, goodsList.map { it.outletName })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            rd_spn_outlet_name.adapter = adapter
        }
    }
    private fun onClickFunction() {
        rd_btn_submit_report.setOnClickListener {
            updateReportData()
        }
    }

    // TODO : rubah
    private fun updateReportData() {
        val extractedValues = mutableListOf<Int>()

        val adapter = ARED_rv_selled_product.adapter as ReportEditProductSoldAdapter

        try {
            for (i in 0 until adapter.itemCount) {
                val viewHolder = ARED_rv_selled_product.findViewHolderForAdapterPosition(i) as ReportEditProductSoldAdapter.MyViewHolder
                val editTextValue = viewHolder.itemView.findViewById<TextInputEditText>(R.id.IRGL_tiet_product_sold)
                val value = editTextValue.text.toString().toIntOrNull() ?: 0

                extractedValues.add(value)
            }

            // TODO : Celah Keamanan, pikirin nanti
            val report = Report(
                0,
                selectedOutlet,
                reportEdited.imageTransportDistance,
                reportEdited.imagePapOutlet,
                extractedValues,
                reportEdited.startTime,
                reportEdited.endTime
            )

            // Call the insertReport function in the ViewModel to insert the new report
            viewModel.updateReport(report)
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
        }
    }

    // 4. tampilkan gambarnya
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(imageFile))
                when (imageMarker) {
                    1 -> rd_iv_pap_outlet.setImageBitmap(bitmap)
                    2 -> rd_iv_transport.setImageBitmap(bitmap)
                }
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}