package com.example.myapplication.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.BuildConfig
import com.example.myapplication.GlobalVariable
import com.example.myapplication.R
import com.example.myapplication.database.Report
import com.example.myapplication.recViewAdapter.ReportCreateProductSoldAdapter
import com.example.myapplication.util.GenerateRandomString
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.util.TimeTools
import com.example.myapplication.viewmodel.MyViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.report_detail.RD_rv_selled_product
import kotlinx.android.synthetic.main.report_detail.rd_btn_submit_report
import kotlinx.android.synthetic.main.report_detail.rd_iv_pap_outlet
import kotlinx.android.synthetic.main.report_detail.rd_iv_transport
import kotlinx.android.synthetic.main.report_detail.rd_spn_outlet_name
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportDetail : AppCompatActivity() {

    private lateinit var reportCreateProductSoldAdapter: ReportCreateProductSoldAdapter
    private lateinit var viewModel: MyViewModel

    private val startTime = TimeTools.getCurrentTime()

    private val CAMERA_REQUEST_CODE = 100
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var imageFile: File

    private var imageMarker = 0
    private lateinit var imagePapOutlet : String
    private lateinit var imageTransport : String

    lateinit var selectedOutlet : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_detail)

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

    private fun setUpRecView() {
        RD_rv_selled_product.layoutManager = LinearLayoutManager(this)

        reportCreateProductSoldAdapter = ReportCreateProductSoldAdapter(emptyList())
        RD_rv_selled_product.adapter = reportCreateProductSoldAdapter

        // get data from database
        viewModel.allGoods.observe(this) { dataList ->
            reportCreateProductSoldAdapter.setData(dataList)
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

        rd_iv_pap_outlet.setOnClickListener{
            imageMarker = 1
            takeImage()
        }

        rd_iv_transport.setOnClickListener{
            imageMarker = 2
            takeImage()
        }

        rd_btn_submit_report.setOnClickListener {
            saveReportData()
        }
    }

    // TODO : check if merchandiser provide all the data needed else toast error
    private fun saveReportData() {
        val extractedValues = mutableListOf<Int>()
        val adapter = RD_rv_selled_product.adapter as ReportCreateProductSoldAdapter

        try {
            for (i in 0 until adapter.itemCount) {
                val viewHolder = RD_rv_selled_product.findViewHolderForAdapterPosition(i) as ReportCreateProductSoldAdapter.MyViewHolder
                val editTextValue = viewHolder.itemView.findViewById<TextInputEditText>(R.id.IRGL_tiet_product_sold)
                val value = editTextValue.text.toString().toIntOrNull() ?: 0

                extractedValues.add(value)
            }

            val report = Report(
                0,
                selectedOutlet,
                imageTransport,
                imagePapOutlet,
                extractedValues,
                startTime,
                TimeTools.getCurrentTime()
            )

            viewModel.insertReport(report)
            finish()
        } catch (e: Exception) {
            // Handle the exception
            Toast.makeText(this, "$e", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "Lengkapi semua data yang dibutuhkan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takeImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
        }
        else takePhoto()
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageFile = createImageFile()

        val uri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this,
                "${BuildConfig.APPLICATION_ID}.provider",
                imageFile
            )
        } else {
            Uri.fromFile(imageFile)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    // 3. simpen gambarnya di folder
    private fun createImageFile(): File {
        val globalVariable = applicationContext as GlobalVariable
        val fileName = "${globalVariable.namaMerchandiser}_${SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())}"
        val dir = File(Environment.getExternalStorageDirectory(), "MD_$fileName")
        if (!dir.exists()) {
            val isCreated = dir.mkdirs()
//            Toast.makeText(this, "Directory created: $isCreated", Toast.LENGTH_SHORT).show()
        }

        val uniqueNaming = GenerateRandomString.generateRandomString(7)

        when (imageMarker) {
            1 -> imagePapOutlet = "$fileName $uniqueNaming"
            else -> imageTransport = "$fileName $uniqueNaming"
        }

        try {
//            Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show()
            return File(dir, "$fileName ${uniqueNaming}.jpg")
        } catch (e: Exception) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
            throw Exception()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            }
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