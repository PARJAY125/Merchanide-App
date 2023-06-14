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
        // Create CSV file after populating the rows list
        val name = "Parjay"
        val fileName = "${name}_${SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())}"
        val dir = File(Environment.getExternalStorageDirectory(), "MD_${fileName}")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "Laporan_${fileName}.csv")

        val header = arrayOf("id", "outlet_name", "transport_distance", "image_before", "image_after", "is_stock_full", "list_goods_ids", "start_time", "end_time")

        val rows = mutableListOf<Array<String?>>()
        viewModel.allReports.observe(this) { reports ->
            rows.clear() // Clear previous data
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

            try {
                val csvWriter = CSVWriter(FileWriter(file))
                csvWriter.writeNext(header)
                for (row in rows) csvWriter.writeNext(row)
                csvWriter.close()
                Toast.makeText(this, "file berhasil dibuat", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "file gagal dibuat", Toast.LENGTH_SHORT).show()
            }
        }
    }
}