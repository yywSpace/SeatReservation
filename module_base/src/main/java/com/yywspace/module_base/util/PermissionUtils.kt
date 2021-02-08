package com.yywspace.module_base.util

import android.content.Context
import android.content.pm.PackageManager
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

object PermissionUtils {
    public fun requestPermission(context: Context, onGranted: (() -> Unit)?, onDenied: (() -> Unit)?, vararg permissions: String) {
        var hasPermission = true
        for (permission in permissions) {
            hasPermission = PackageManager.PERMISSION_GRANTED ==
                    context.packageManager.checkPermission(permission, context.packageName)
            if (!hasPermission) break
        }
        if (!hasPermission) {
            AndPermission.with(context)
                    .runtime()
                    .permission(permissions)
                    .onGranted {
                        onGranted?.invoke()
                    }
                    .onDenied {
                        onDenied?.invoke()
                    }
                    .start()
        } else {
            onGranted?.invoke()
        }
    }

}