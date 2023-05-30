package com.example.myapplication.util

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.report_detail.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class TakePictureTools {
    companion object {

        // Function to launch the camera app
//        private fun dispatchTakePictureIntent(imageViewId: Int, packageManager : PackageManager, context : AppCompatActivity) {
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                // Ensure that there's a camera activity to handle the intent
//                takePictureIntent.resolveActivity(packageManager)?.also {
//                    // Create the File where the photo should go
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        null
//                    }
//                    // Continue only if the File was successfully created
//                    photoFile?.also {
//                        val photoURI: Uri = FileProvider.getUriForFile(
//                            context,
//                            "com.example.android.fileprovider",
//                            it
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                        startActivityForResult(takePictureIntent, imageViewId)
//                    }
//                }
//            }
//        }
//
//        // Function to create a unique image file
//        @Throws(IOException::class)
//        private fun createImageFile(): File {
//            // Create an image file name
//            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(
//                Date()
//            )
//            val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            return File.createTempFile(
//                "JPEG_${timeStamp}_",
//                ".jpg",
//                storageDir
//            ).apply {
//                // Save a file: path for use with ACTION_VIEW intents
//                currentPhotoPath = absolutePath
//            }
//        }
//
//        // Retrieve the captured image
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)
//
//            if (resultCode == Activity.RESULT_OK) {
//                val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
//                when (requestCode) {
//                    1 -> rd_iv_before.setImageBitmap(imageBitmap)
//                    2 -> rd_iv_after.setImageBitmap(imageBitmap)
//                }
//            }
//        }
    }
}