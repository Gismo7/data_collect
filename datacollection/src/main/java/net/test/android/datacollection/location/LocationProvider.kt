package net.test.android.datacollection.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import io.reactivex.Emitter
import io.reactivex.Observable

class LocationProvider(private val context: Context,
                       private var updateInterval: Long = UPDATE_INTERVAL,
                       private var fastestInterval: Long = FASTEST_INTERVAL) : LocationCallback() {

    companion object {
        private const val UPDATE_INTERVAL = (30 * 1000).toLong()
        private const val FASTEST_INTERVAL: Long = 2000
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var actions: Actions? = null
    private lateinit var locationRequest: LocationRequest

    init {
        initLocationRequest()
    }

    @RequiresPermission(allOf = [(Manifest.permission.ACCESS_FINE_LOCATION), (Manifest.permission.ACCESS_COARSE_LOCATION)])
    fun getLocationListener(): Observable<Location> =
            Observable.create { emitter: Emitter<Location> ->

                connect(object : Actions {
                    override fun onLocationChanged(location: Location) {
                        emitter.onNext(location)
                    }

                    override fun onLastLocation(location: Location?) {
                        location?.let { emitter.onNext(it) }
                    }
                })
            }.doFinally { disconnect() }

    @RequiresPermission(allOf = [(Manifest.permission.ACCESS_FINE_LOCATION), (Manifest.permission.ACCESS_COARSE_LOCATION)])
    fun connect(actions: Actions) {
        this.actions = actions
        initFusedLocationClient()
        start()
    }

    fun disconnect() {
        stopLocationUpdates()
    }

    private fun initLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = updateInterval
        locationRequest.fastestInterval = fastestInterval
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun initFusedLocationClient() {
        if (mFusedLocationClient == null) {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        }
    }

    private fun start() {
        getLastLocation()
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient?.apply {
            lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    actions?.onLastLocation(location)
                }
            }
        }
    }

    private fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(this)
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        mFusedLocationClient?.requestLocationUpdates(locationRequest, this, null)
    }

    override fun onLocationResult(locationResult: LocationResult) {
        super.onLocationResult(locationResult)
        actions?.onLocationChanged(locationResult.lastLocation)
    }

    interface Actions {
        fun onLocationChanged(location: Location)

        fun onLastLocation(location: Location?)
    }
}