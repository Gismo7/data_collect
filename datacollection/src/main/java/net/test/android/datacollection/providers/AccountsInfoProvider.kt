package net.test.android.datacollection.providers

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.content.Context
import io.reactivex.Single
import net.test.android.datacollection.entities.Account
import net.test.android.datacollection.entities.MobileDevice

class AccountsInfoProvider(private val context: Context) : InfoProvider {

    override fun requiredDangerousPermissions() = listOf(Manifest.permission.GET_ACCOUNTS)

    override fun putInfo(mobileDeviceInfo: MobileDevice): Single<MobileDevice> {
        if (isPermissionGranted(context)) {
            mobileDeviceInfo.accounts = getAccounts()
        }
        return Single.just(mobileDeviceInfo)
    }

    @SuppressLint("MissingPermission")
    private fun getAccounts(): List<Account> {
        val accounts = AccountManager.get(context).accounts
        val accountsList = arrayListOf<Account>()
        accounts.mapTo(accountsList) { Account(it.type, it.name) }
        return accountsList
    }
}