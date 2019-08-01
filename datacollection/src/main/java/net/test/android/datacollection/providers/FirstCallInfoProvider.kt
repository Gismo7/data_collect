package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.CallLog
import io.reactivex.Single
import net.test.android.core.util.FormatUtils
import net.test.android.datacollection.entities.MobileDevice
import java.util.*

class FirstCallInfoProvider(private val context: Context,
                            private val serializedName: String = "call_first_date") : InfoProvider {

    override fun requiredDangerousPermissions() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                listOf(Manifest.permission.READ_CALL_LOG)
            } else {
                listOf(Manifest.permission.READ_CONTACTS)
            }

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val data = mutableMapOf<String, Any?>()
            mobileDeviceInfo.data?.let { data.putAll(it) }
            data[serializedName] = getCallInfo()
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    @SuppressLint("MissingPermission")
    private fun getCallInfo(): String? {
        val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.DATE),
                null,
                null,
                "${CallLog.Calls.DATE} ASC")
        var date: Date? = null
        cursor?.let {
            date = if (cursor.moveToFirst()) {
                Date(cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)))
            } else {
                null
            }
            cursor.close()
        }
        return date?.let {
            FormatUtils.formatDate(date, FormatUtils.DATE_TIME_PATTERN_WITH_SECONDS)
        }
    }
}