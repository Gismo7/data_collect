package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.telephony.TelephonyManager
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice

class PhoneNumberInfoProvider(private val context: Context) : InfoProvider {

    override fun requiredDangerousPermissions() = listOf(Manifest.permission.READ_PHONE_STATE)

    @SuppressLint("MissingPermission", "HardwareIds")
    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            mobileDeviceInfo.simPhone = telephonyManager.line1Number
        }
        return Single.just(mobileDeviceInfo)
    }

}