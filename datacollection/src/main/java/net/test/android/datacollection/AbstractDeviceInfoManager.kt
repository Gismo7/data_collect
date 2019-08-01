package net.test.android.datacollection

import android.content.Context
import net.test.android.datacollection.permissions.PermissionsUtils
import net.test.android.datacollection.providers.InfoProvider

abstract class AbstractDataCollectManager(protected val context: Context) {

    //Регистрируем провайдеры
    abstract var infoProviders: List<InfoProvider>

    /**
     * Возвращает список непредоставленых прав необходимых для сбора информации
     * @return список непредоставленых прав
     */
    fun getNotGrantedPermissions(): List<String> {
        val result = mutableListOf<String>()
        infoProviders.forEach { infoProvider ->
            infoProvider.requiredDangerousPermissions()
                    .filter { permission ->
                        !PermissionsUtils.hasPermission(context, permission)
                    }
                    .forEach { permission -> result.add(permission) }
        }
        return result
    }

    abstract fun isCollectDeviceData(): Boolean

    abstract fun onDeviceDataSend()
}