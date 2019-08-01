package net.test.android.datacollection.providers

import android.Manifest
import android.content.Context
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import io.reactivex.Single
import net.test.android.core.util.FormatUtils
import net.test.android.datacollection.entities.MobileDevice
import java.util.*

/**
 * для доступа к смс требуется API версии 19 и выше
 * проверка встроенна в метод putInfo,
 * поэтому использована аннотация SuppressLint("NewApi")
 */
class FirstSmsInfoProvider(private val context: Context,
                           private val serializedName: String = "sms_first_date") : InfoProvider {

    override fun requiredDangerousPermissions() = listOf(Manifest.permission.READ_SMS)

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isPermissionGranted(context)) {
            val data = mutableMapOf<String, Any?>()
            mobileDeviceInfo.data?.let { data.putAll(it) }
            data[serializedName] = getSmsInfo()
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getSmsInfo(): String? {
        val columnName = "MIN"
        val cursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI,
                arrayOf("MIN(${Telephony.Sms.DATE_SENT}) AS $columnName"),
                null,
                null,
                null)
        var date: Date? = null
        cursor?.let {
            date = if (cursor.moveToFirst()) {
                Date(cursor.getLong(cursor.getColumnIndex(columnName)))
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