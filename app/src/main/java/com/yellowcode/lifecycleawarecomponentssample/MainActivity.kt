package com.yellowcode.lifecycleawarecomponentssample

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var myLocationManager: MyLocationManager

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocationListener()
        } else {
            Toast.makeText(this, "Bạn chưa có quyền truy cập vào GPS của thiết bị", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION_CODE)
        } else {
            setupLocationListener()
        }
    }

    private fun setupLocationListener() {
        myLocationManager = MyLocationManager(this) { location ->
            tvContent.text = location.latitude.toString() + ", " + location.longitude
        }
        lifecycle.addObserver(myLocationManager)
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1
    }
}
