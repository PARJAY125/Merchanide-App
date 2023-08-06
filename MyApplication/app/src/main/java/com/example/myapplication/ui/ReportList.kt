package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.example.myapplication.GlobalVariable
import com.example.myapplication.R
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.viewmodel.MyViewModel
import com.opencsv.CSVWriter
import kotlinx.android.synthetic.main.report_list.rl_btn_add_report
import kotlinx.android.synthetic.main.report_list.rl_btn_print_report
import kotlinx.android.synthetic.main.report_list.rl_tl_report_tableLayout
import kotlinx.android.synthetic.main.report_list.rl_tv_refresh
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale

class ReportList : AppCompatActivity() {
    private lateinit var viewModel: MyViewModel

    companion object val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1

    private var goodsPriceList : MutableList<Double> = mutableListOf()
    private var headerMiddle : MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_list)

        initViewModel()
        setTable()
        onClickFunction()
    }

    private fun initViewModel() {
        viewModel = InitDatabase.initDatabase(this)

        viewModel.allGoods.observe(this) { goods ->
            headerMiddle.clear()
            for (good in goods) {
                headerMiddle.add("${good.goodsName} @${good.goodsPrice}")
                goodsPriceList.add(good.goodsPrice)
            }
        }
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
                    val intent = Intent(this, ReportEditDetail::class.java)
                    intent.putExtra("report", report)
                    startActivity(intent)
                }

                // Add the table row to the table view
                rl_tl_report_tableLayout.addView(tableRow)
            }
        }
    }

    private fun exportToCsv() {
        val globalVariable = applicationContext as GlobalVariable
        val fileName = "${globalVariable.namaMerchandiser}_${SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())}"
        val dir = File(Environment.getExternalStorageDirectory(), "MD_${fileName}")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "Laporan_${fileName}.csv")

        // List Report
        val headerStart = arrayOf("id", "Nama Outlet", "Foto Odometer", "Foto di Outlet")

        val headerEnd = arrayOf("start_time", "end_time", "total waktu", "total keuntungan")
        val header = headerStart + headerMiddle.toTypedArray() + headerEnd

        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val rows = mutableListOf<Array<String?>>()
        viewModel.allReports.observe(this) { reports ->
            rows.clear() // Clear previous data
            for (report in reports) {
                // TODO : Ini juga pakein for loop
                val rowStart = arrayOf(
                    report.id.toString(),
                    report.outletName,
                    report.imageTransportDistance,
                    report.imagePapOutlet
                )
                // rows += rowStart

                var totalEarnings = 0.0
                val rowMiddle = mutableListOf<String>()
                for (i in report.listGoodsSold.indices) {
                    totalEarnings += goodsPriceList[i] * report.listGoodsSold[i]
                    rowMiddle.add(report.listGoodsSold[i].toString())
                }
                // rows += rowMiddle.toTypedArray()

                val startTime = LocalTime.parse(report.startTime, formatter)
                val endTime = LocalTime.parse(report.endTime, formatter)
                val rowEnd : Array<String?> = arrayOf(
                    // report.listGoodsSold.joinToString(","),         // TODO : Ini juga pakein for loop
                    report.startTime,
                    report.endTime,
                    startTime.until(endTime, ChronoUnit.MINUTES).toString(),// total waktu
                    totalEarnings.toString() // total keuntungan
                )
                // rows += rowEnd
                val rowCombined = rowStart + rowMiddle.toTypedArray() + rowEnd
                rows.add(rowCombined)
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