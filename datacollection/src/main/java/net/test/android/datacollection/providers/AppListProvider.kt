package net.test.android.datacollection.providers

import android.content.Context
import android.content.pm.PackageManager
import io.reactivex.Single
import net.test.android.datacollection.entities.MobileDevice


class AppListProvider(private val context: Context,
                      private val serializedName: String = "app_list") : InfoProvider {

    override fun requiredDangerousPermissions() = listOf<String>()

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            val data = mutableMapOf<String, Any?>()
            mobileDeviceInfo.data?.let { data.putAll(it) }
            data[serializedName] = getAppList()
            mobileDeviceInfo.data = data
        }
        return Single.just(mobileDeviceInfo)
    }

    private fun getAppList(): List<String> {

        val result = mutableListOf<String>()
        context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA).forEach {
            result.add(it.packageName)
        }
        return result
    }
}