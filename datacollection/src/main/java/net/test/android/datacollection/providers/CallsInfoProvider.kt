package net.test.android.datacollection.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.CallLog
import io.reactivex.Single
import net.test.android.datacollection.entities.Contact
import net.test.android.datacollection.entities.MobileDevice
import java.util.*

class CallsInfoProvider(private val context: Context,
                        private val callsCount: Int = 100) : InfoProvider {

    override fun requiredDangerousPermissions() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                listOf(Manifest.permission.READ_CALL_LOG)
            } else {
                listOf(Manifest.permission.READ_CONTACTS)
            }

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MONTH, -1)
            mobileDeviceInfo.contacts = getCalls(calendar.timeInMillis, callsCount)
        }
        return Single.just(mobileDeviceInfo)
    }

    @SuppressLint("MissingPermission")
    private fun getCalls(overDate: Long, calls: Int): List<Contact> {
        val cursor = context.contentResolver.query(CallLog.Calls.CONTENT_URI,
                arrayOf(CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER),
                "${CallLog.Calls.DATE} > ?",
                arrayOf(overDate.toString()),
                "${CallLog.Calls.DATE} DESC" + " LIMIT $calls")

        val contacts = mutableListOf<Contact>()

        cursor?.let {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                if (name != null) {
                    contacts.add(Contact(name, phoneNumber))
                }
            }
            cursor.close()
        }
        return contacts
    }
}