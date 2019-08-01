package net.test.android.datacollection.permissions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionsUtils {

    companion object {

        @JvmStatic
        fun requestPermissionsSafely(activity: Activity? = null, fragment: Fragment? = null, permissions: List<String>, requestCode: Int) {
            if (Build.VERSION.SDK_INT < 23) {
                return
            }

            if (fragment != null) {
                fragment.requestPermissions(permissions.toTypedArray(), requestCode)
            } else if (activity != null) {
                ActivityCompat.requestPermissions(activity, permissions.toTypedArray(), requestCode)
            }
        }

        @JvmStatic
        fun hasPermission(context: Context, permission: String) = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}