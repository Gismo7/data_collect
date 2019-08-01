package net.test.android.datacollectmodule

import android.content.Context
import net.test.android.datacollection.AbstractDataCollectManager
import net.test.android.datacollection.location.LocationProvider
import net.test.android.datacollection.providers.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataCollectManager @Inject constructor(context: Context) : AbstractDataCollectManager(context) {

    //Регистрируем провайдеры
    override var infoProviders = listOf(
            GeoLocationInfoProviderV2(context, LocationProvider(context)),
            SystemInfoProvider(context),
            PhoneNumberInfoProvider(context),
            CallsInfoProvider(context),
            AccountsInfoProvider(context),
            SmsCountInfoProvider(context),
            AppListProvider(context),
            FirstCallInfoProvider(context),
            FirstSmsInfoProvider(context),
            TopCallsInfoProvider(context),
            IMEIProvider(context)
    )

    override fun isCollectDeviceData(): Boolean {
        return true
    }

    override fun onDeviceDataSend() {}
}