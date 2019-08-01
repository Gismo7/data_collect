package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice

class IMEIProvider(private val context: Context,
                   private val serializedName: String = "imei") : InfoProvider {

    override fun requiredDangerousPermissions() =
            listOf(Manifest.permission.READ_PHONE_STATE)

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val data = mutableMapOf<String, Any?>()
            mobileDeviceInfo.data?.let { data.putAll(it) }
            data[serializedName] = getIMEI()
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    private fun getIMEI(): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            telephonyManager.imei
        } else {
            telephonyManager.deviceId
        }
    }
}