package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.myapplication.R
import com.example.myapplication.database.Report
import com.example.myapplication.database.RoomDatabase
import com.example.myapplication.repository.MyRepository
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import com.example.myapplication.viewmodel.MyViewModelFactory
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.report_list.*
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportList : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var reportList: List<Report>

    companion object val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list)

        initViewModel()
        setTable()
        onClickFunction()

    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun onClickFunction() {
        rl_btn_add_report.setOnClickListener {
            startActivity(Intent(this, ReportDetail::class.java))
        }

        rl_tv_refresh.setOnClickListener {
            finish()
            startActivity(intent)
        }

        rl_btn_print_report.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
            }
            else exportToCsv()
        }
    }

    private fun setTable() {
        // Get all reports from the view model
        viewModel.allReports.observe(this) { reports ->
            // Iterate through the list of reports and add them to the table view
            for (report in reports) {
                // Create a new table row for the report
                val tableRow = TableRow(this)

                val outletNameTextView = TextView(this)
                val startToEndTextView = TextView(this)

                // create new LayoutParams for the TextViews
                val outletNameLayoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2.5f)
                val startToEndLayoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)

                // set the LayoutParams for the TextViews
                outletNameTextView.layoutParams = outletNameLayoutParams
                startToEndTextView.layoutParams = startToEndLayoutParams

                outletNameTextView.text = report.outletName
                startToEndTextView.text = "${report.startTime} - ${report.endTime}"

                // add the TextView to the TableRow
                tableRow.addView(outletNameTextView)
                tableRow.addView(startToEndTextView)
                tableRow.setPadding(10)

                // handle row click event here
                tableRow.setOnClickListener {
                    // todo (optional) : procedure should -> report outlet name -> report id
                    // i dont like this but this is work
                    // Do something with the report data, such as show it in a details view
                    val intent = Intent(this, ReportDetail::class.java)
                    intent.putExtra("reportId", report.id)
                    startActivity(intent)
                }

                // Add the table row to the table view
                rl_tl_report_tableLayout.addView(tableRow)
            }
        }
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