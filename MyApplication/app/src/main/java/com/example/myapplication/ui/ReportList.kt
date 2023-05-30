package com.example.myapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
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
import kotlinx.android.synthetic.main.report_list.*

class ReportList : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private lateinit var reportList: List<Report>

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

        rl_fab_add_outlet.setOnClickListener{

        }

        rl_tv_refresh.setOnClickListener {
            finish()
            startActivity(intent)
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
}