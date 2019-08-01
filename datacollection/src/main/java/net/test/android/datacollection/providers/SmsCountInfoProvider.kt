package net.test.android.datacollection.providers

import android.Manifest
import android.content.Context
import android.os.Build
import android.provider.Telephony
import androidx.annotation.RequiresApi
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice
import java.util.*

/**
 * для доступа к смс требуется API версии 19 и выше
 * проверка встроенна в метод putInfo,
 * поэтому использована аннотация SuppressLint("NewApi")
 */
class SmsCountInfoProvider(private val context: Context,
                           private val serializedName: String = "sms_count180",
                           private val dayCount: Int = 180) : InfoProvider {

    override fun requiredDangerousPermissions() = listOf(Manifest.permission.READ_SMS)

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isPermissionGranted(context)) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -dayCount)
            val data = mutableMapOf<String, Any?>()
            mobileDeviceInfo.data?.let { data.putAll(it) }
            data[serializedName] = getSmsMessagesCount(calendar.timeInMillis)
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun getSmsMessagesCount(overDate: Long): Int {
        val cursor = context.contentResolver.query(Telephony.Sms.CONTENT_URI,
                arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY),
                "${Telephony.Sms.DATE} > ?",
                arrayOf(overDate.toString()),
                "${Telephony.Sms.DATE} DESC")

        var smsCount = 0;
        cursor?.let {
            smsCount = cursor.count
            cursor.close()
        }
        return smsCount
    }
}
