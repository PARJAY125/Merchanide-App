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

        AM_btn_record_gps.setOnClickListener {
            getLocation()
        }

        AM_btn_to_am2.setOnClickListener {
            startActivity(Intent(this, ReportDetail::class.java))
        }

        AM_btn_to_am3.setOnClickListener {
            startActivity(Intent(this, ReportList::class.java))
        }

        AM_btn_to_am4.setOnClickListener {
            startActivity(Intent(this, DemoCamera::class.java))
        }

        AM_btn_to_am5.setOnClickListener {
            startActivity(Intent(this, DemoKonversi::class.java))
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission is granted
                    // Perform your desired action here
                    // ...
                } else {
                    // Location permission is denied
                    // Handle the case when the permission is denied
                    // ...
                }
            }

            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Storage permission is granted
                    // Perform your desired action here
                    // ...
                } else {
                    // Storage permission is denied
                    // Handle the case when the permission is denied
                    // ...
                }
            }
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0f, locationListener)
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            // Mendapatkan lokasi terbaru
            val latitude = location.latitude
            val longitude = location.longitude

            AM_tv_gps_location.text = "Lokasi :  ($latitude | $longitude)"

            // Hentikan pembaruan lokasi setelah mendapatkan hasil pertama
            locationManager.removeUpdates(this)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }
}