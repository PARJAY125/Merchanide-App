package com.example.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_CODE = 1
    private val STORAGE_PERMISSION_CODE = 2

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkingPermission()

        AM_btn_to_am2.setOnClickListener {
            startActivity(Intent(this, ReportDetail::class.java))
        }

        AM_btn_to_am3.setOnClickListener {
            startActivity(Intent(this, ReportList::class.java))
        }

        AM_btn_to_data_barang.setOnClickListener {
            startActivity(Intent(this, DataBarang::class.java))
        }

        AM_btn_to_data_outlet.setOnClickListener {
            startActivity(Intent(this, DataOutlet::class.java))
        }
    }

    private fun checkingPermission() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        val permissionsToRequest = mutableListOf<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                LOCATION_PERMISSION_CODE // Use a unique request code for each permission
            )
        } else {
            // Permissions are already granted
            // Perform your desired action here
            // ...
        }
    }
}