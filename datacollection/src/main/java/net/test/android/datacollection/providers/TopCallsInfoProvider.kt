package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.CallLog
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice

class TopCallsInfoProvider(private val context: Context,
                           private val serializedName: String = "top_calls",
                           private val callsCount: Int = 1000,
                           private val topCallsCount: Int = 4) : InfoProvider {

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
            data[serializedName] = getTopCalls()
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    @SuppressLint("MissingPermission")
    private fun getTopCalls(): List<String> {
        val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.NUMBER),
                null,
                null,
                "${CallLog.Calls.NUMBER} ASC")

        val map = mutableMapOf<String, Int>()
        cursor?.let {
            val columnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            var i = 0
            while (cursor.moveToNext() && i++ < callsCount) {
                val number = cursor.getString(columnIndex)
                val count = map[number]
                map[number] = if (count == null) 1 else count + 1
            }
            cursor.close()
        }

        val result = mutableListOf<String>()

        map.toList()
                .sortedByDescending { (_, value) -> value }
                .toMap()
                .forEach { (number, _) ->
                    if (result.size < topCallsCount) {
                        result.add(number)
                    } else {
                        return@forEach
                    }
                }

        return result
    }
}