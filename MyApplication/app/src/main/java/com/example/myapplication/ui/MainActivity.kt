package com.example.myapplication.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.GlobalVariable
import com.example.myapplication.R
import com.example.myapplication.database.Outlet
import com.example.myapplication.dialog.DialogOneInput
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DialogOneInput.DialogListener {

    private val LOCATION_AND_STORAGE_PERMISSION_CODE = 1

    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        checkingPermission()

        val globalVariable = applicationContext as GlobalVariable
        if (globalVariable.namaMerchandiser != "") AM_tv_md_name.text = globalVariable.namaMerchandiser
        else AM_tv_md_name.text = "Nama Anda Belum Terdaftar"

        AM_btn_rename_merchandise.setOnClickListener {
            val dialogFragment = DialogOneInput()
            dialogFragment.show(supportFragmentManager, "ChangeMDName")
        }

        // TODO : warning if belum set nama ato barang ato outlet
        AM_btn_to_am3.setOnClickListener {
            if (globalVariable.namaMerchandiser == "")
                Toast.makeText(this, "Masukkan Nama Terlebih Dahulu", Toast.LENGTH_SHORT).show()
            else startActivity(Intent(this, ReportList::class.java))
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
                LOCATION_AND_STORAGE_PERMISSION_CODE // Use a unique request code for each permission
            )
        } else {
            // Permissions are already granted
            // Perform your desired action here
            // ...
        }
    }

    override fun onDialogResult(value: String) {
        Toast.makeText(this, "nama anda $value", Toast.LENGTH_SHORT).show()
        val globalVariable = applicationContext as GlobalVariable
        globalVariable.namaMerchandiser = value
        AM_tv_md_name.text = value
    }

    override fun onEditDialogResult(outlet: Outlet) {}
}