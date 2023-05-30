//import android.app.Activity
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.provider.MediaStore
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import kotlinx.android.synthetic.main.activity_main.*
//import java.io.IOException
//
//class MainActivity : AppCompatActivity() {
//
//    private val IMAGE_PICK_CODE = 1000
//    private val CAMERA_PICK_CODE = 1001
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        button_pick_image.setOnClickListener {
//            checkPermission()
//        }
//    }
//
//    private fun checkPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            pickImageFromGallery()
//        } else {
//            requestPermission()
//        }
//    }
//
//    private fun requestPermission() {
//        ActivityCompat.requestPermissions(this,
//            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//            IMAGE_PICK_CODE)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<out String>, grantResults: IntArray) {
//        when (requestCode) {
//            IMAGE_PICK_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] ==
//                    PackageManager.PERMISSION_GRANTED) {
//                    pickImageFromGallery()
//                } else {
//                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//    private fun pickImageFromGallery() {
//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
//            image_view.setImageURI(data?.data)
//        }
//    }
//}