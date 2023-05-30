package com.example.myapplication.ui

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.activity_demo_konversi.*
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

/*
// DONE
// SUKSES - data dummy.csv
// SUKSES - folder khusus satu folder ama imagenya nanti
// SUKSES - pakai room database
*/

class DemoKonversi : AppCompatActivity() {

    companion object val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1

    private lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_konversi)

        initViewModel()

        ADK_Btn_save_data.setOnClickListener {

            if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
            }
            else exportToCsv()
        }
    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun exportToCsv() {
        val header = arrayOf("id", "outlet_name", "transport_distance", "image_before", "image_after", "is_stock_full", "list_goods_ids", "start_time", "end_time")
        val rows = mutableListOf<Array<String?>>()
        viewModel.allReports.observe(this) { reports ->
            for (report in reports) {
                val row = arrayOf(
                    report.id.toString(),
                    report.outletName,
                    report.transportDistance.toString(),
                    report.imageBefore,
                    report.imageAfter,
                    report.isStockFull.toString(),
                    report.listGoodsIds.joinToString(","),
                    report.startTime,
                    report.endTime
                )
                rows.add(row)
            }
        }

        val name = "Parjay"
        val fileName = "${name}_${SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())}"
        val dir = File(Environment.getExternalStorageDirectory(), "MD_${fileName}")
        if (!dir.exists()) {
            val isCreated = dir.mkdirs()
//            Toast.makeText(this, "Directory created: $isCreated", Toast.LENGTH_SHORT).show()
        }

        val file = File(dir, "Laporan_${fileName}.csv")
        try {
            val csvWriter = CSVWriter(FileWriter(file))
            csvWriter.writeNext(header)
            for (row in rows) csvWriter.writeNext(row)
            csvWriter.close()
//            Toast.makeText(this, "File created at path: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
        }
    }
}