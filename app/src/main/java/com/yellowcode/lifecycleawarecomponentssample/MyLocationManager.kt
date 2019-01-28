package com.yellowcode.lifecycleawarecomponentssample

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast

/**
 * Lớp này phụ trách việc kết nối lấy thông tin location của user, và trả thông tin này về cho lớp gọi
 * content: Context của lớp gọi
 * callback: Trả kết quả về cho callback
 */
@SuppressWarnings("MissingPermission")
class MyLocationManager(private val context: Context, private val callback: (Location) -> Unit): LifecycleObserver {

    private var mLocationManager: LocationManager? = null

    /**
     * Phương thức bắt đầu kết nối với Google Service để lấy thông tin tọa độ
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        // Force an update with the last location, if available.
        val lastLocation = mLocationManager?.getLastKnownLocation(
            LocationManager.GPS_PROVIDER
        )
        if (lastLocation != null) {
            locationListener.onLocationChanged(lastLocation)
        }

        Toast.makeText(context, "MyLocationManager started", Toast.LENGTH_SHORT).show()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stop() {
        if (mLocationManager == null) {
            return
        }
        mLocationManager?.removeUpdates(locationListener)
        mLocationManager = null

        Toast.makeText(context, "MyLocationManager paused", Toast.LENGTH_SHORT).show()
    }

    /**
     * Custom lại LocationListen để có thể trả thông tin location về cho lớp gọi thông qua callback
     */
    val locationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            callback.invoke(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }
}