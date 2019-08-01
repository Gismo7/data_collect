package net.test.android.datacollection.providers

import android.content.Context
import android.os.Build
import io.reactivex.Single
import net.test.android.datacollection.entities.DeviceModel
import net.test.android.datacollection.entities.MobileDevice

class SystemInfoProvider(private val context: Context) : InfoProvider {

    override fun requiredDangerousPermissions() = listOf<String>()

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val vendor = Build.BRAND
            val model = Build.MODEL
            val platform = "Android"
            val osVersion = Build.VERSION.SDK_INT.toString()
            mobileDeviceInfo.deviceModel = DeviceModel(vendor, model, platform, osVersion)
        }
        return Single.just(mobileDeviceInfo)
    }
}