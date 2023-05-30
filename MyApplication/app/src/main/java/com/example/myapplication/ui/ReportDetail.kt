package com.example.myapplication.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.myapplication.BuildConfig
import com.example.myapplication.R
import com.example.myapplication.database.*
import com.example.myapplication.util.GenerateRandomString
import com.example.myapplication.util.GpsTools
import com.example.myapplication.util.InitDatabase
import com.example.myapplication.util.TimeTools
import com.example.myapplication.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_demo_camera.*
import kotlinx.android.synthetic.main.report_detail.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ReportDetail : AppCompatActivity() {

    private lateinit var viewModel: MyViewModel
    private var reportId : Int? = null

    private val startTime = TimeTools.getCurrentTime()

    private val CAMERA_REQUEST_CODE = 100
    private val PERMISSION_REQUEST_CODE = 200
    private lateinit var imageFile: File

    private var imageMarker = 0
    private lateinit var imageAfter : String
    private lateinit var imageBefore : String
    private lateinit var imageTransport : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_detail)

        // todo : check if intent extra available
        checkIntentExtra()
        initUi()
        initDatabase()
        onClickFunction()
    }

    private fun checkIntentExtra() {
        reportId = intent.getIntExtra("reportId", -1)
        if(reportId != -1 ) rd_tiet_outlet_name.hint = "outlet id : $reportId"
    }

    private fun initDatabase() {
        viewModel = InitDatabase.initDatabase(this)
    }

    private fun onClickFunction() {
        rd_btn_set_gps_location.setOnClickListener {
           GpsTools.setGPSLocation(rd_wv_gps_location, rd_tv_gps_location)
        }

        rd_iv_before.setOnClickListener{
            imageMarker = 1
            takeImage()
        }

        rd_iv_after.setOnClickListener{
            imageMarker = 2
            takeImage()
        }

        rd_iv_transport.setOnClickListener{
            imageMarker = 3
            takeImage()
        }

        rd_btn_submit_report.setOnClickListener {
            saveReportData()
        }
    }

    private fun initUi() {
        GpsTools.setGps(rd_wv_gps_location)
        GpsTools.gpsSetting(rd_wv_gps_location)
    }

    private fun saveReportData() {
        // todo : change this dummy data to real user input data
        val dummyGoodsListId = listOf(1, 2, 3)

        val report = Report(
            0,
            rd_tiet_outlet_name.text.toString(),
            imageTransport,
            imageAfter,
            imageBefore,
            true,
            dummyGoodsListId,
            startTime,
            TimeTools.getCurrentTime()
        )

        // Call the insertReport function in the ViewModel to insert the new report
        viewModel.insertReport(report)
        finish()
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
        val name = "Parjay"
        val fileName = "Outlet_${name}_${SimpleDateFormat("yyyy_MM_dd", Locale.getDefault()).format(Date())}"
        val dir = File(Environment.getExternalStorageDirectory(), "MD_$fileName")
        if (!dir.exists()) {
            val isCreated = dir.mkdirs()
//            Toast.makeText(this, "Directory created: $isCreated", Toast.LENGTH_SHORT).show()
        }

        when (imageMarker) {
            1 -> imageAfter = dir.toString()
            2 -> imageBefore = dir.toString()
            else -> imageTransport = dir.toString()
        }

        try {
//            Toast.makeText(this, "sucess", Toast.LENGTH_SHORT).show()
            return File(dir, "$fileName ${GenerateRandomString.generateRandomString(7)}.jpg")
        } catch (e: Exception) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show()
            throw Exception()
        }
    }

    // 4. simpen alamat gambar nya di room database

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

    // 5. tampilkan gambarnya
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, Uri.fromFile(imageFile))
                when (imageMarker) {
                    1 -> rd_iv_before.setImageBitmap(bitmap)
                    2 -> rd_iv_after.setImageBitmap(bitmap)
                    3 -> rd_iv_transport.setImageBitmap(bitmap)
                }
                // else imageTransport
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}