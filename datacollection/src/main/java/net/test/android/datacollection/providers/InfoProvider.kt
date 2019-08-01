package net.test.android.datacollection.providers

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice

interface InfoProvider {

    fun requiredDangerousPermissions(): List<String>

    fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice>

    fun isPermissionGranted(mContext: Context): Boolean {
        for (permission in requiredDangerousPermissions()) {
            if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }
}
