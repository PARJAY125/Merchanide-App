package com.example.myapplication.ui//package com.example.myapplication
//
//import android.Manifest
//import android.content.Context
//import android.content.IntentSender.SendIntentException
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.location.LocationManagerCompat.getCurrentLocation
//import com.google.android.gms.common.api.ApiException
//import com.google.android.gms.common.api.ResolvableApiException
//import com.google.android.gms.location.*
//import com.google.android.gms.tasks.OnCompleteListener
//import com.google.android.gms.tasks.Task
//import kotlinx.android.synthetic.main.activity_main.*
//
//
//class MainActivityOri : AppCompatActivity() {
//
//    private val locationRequest: LocationRequest? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        AM_btn_record_gps.setOnClickListener {
//
//            getCurrentLocation()
//        }
//
//        AM_btn_take_photo.setOnClickListener {
//
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode != 1) return
//        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) return
//
//        if (isGPSEnabled()) getCurrentLocation()
//        else turnOnGPS()
//    }
//
//    private fun turnOnGPS() {
//        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
//        builder.setAlwaysShow(true)
//        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(
//            applicationContext
//        ).checkLocationSettings(builder.build())
//        result.addOnCompleteListener { task ->
//            try {
//                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
//                Toast.makeText(this@MainActivityOri, "GPS is already tured on", Toast.LENGTH_SHORT)
//                    .show()
//            } catch (e: ApiException) {
//                when (e.statusCode) {
//                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
//                        val resolvableApiException = e as ResolvableApiException
//                        resolvableApiException.startResolutionForResult(this@MainActivityOri, 2)
//                    } catch (ex: SendIntentException) {
//                        ex.printStackTrace()
//                    }
//                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
//                }
//            }
//        }
//    }
//
//    private fun isGPSEnabled(): Boolean {
//        var locationManager: LocationManager? = null
//        var isEnabled = false
//        if (locationManager == null) {
//            locationManager = getSystemService<Any>(Context.LOCATION_SERVICE) as LocationManager
//        }
//        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        return isEnabled
//    }
//}