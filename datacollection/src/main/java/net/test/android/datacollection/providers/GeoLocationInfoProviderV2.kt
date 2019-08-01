package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice
import net.test.android.datacollection.location.LocationProvider
import java.util.concurrent.TimeUnit

class GeoLocationInfoProviderV2(private val context: Context,
                                private val locationProvider: LocationProvider,
                                private val serializedName: String = "location") : InfoProvider {
    override fun requiredDangerousPermissions() =
            listOf(Manifest.permission.ACCESS_COARSE_LOCATION)

    @SuppressLint("MissingPermission")
    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        return Single.just(mobileDeviceInfo)
                .flatMap { info ->
                    if (isPermissionGranted(context)) {
                        locationProvider.getLocationListener()
                                .first(Location(""))
                                .timeout(5, TimeUnit.SECONDS, Single.just(Location("")))
                                .map { location ->
                                    if (location.provider.isNotEmpty()) {
                                        val data = mutableMapOf<String, Any?>()
                                        info.data?.let { data.putAll(it) }
                                        data[serializedName] = mapOf("latitude" to location.latitude, "longitude" to location.longitude)
                                        info.data = data
                                    }
                                    info
                                }
                    } else {
                        Single.just(info)
                    }
                }
    }
}