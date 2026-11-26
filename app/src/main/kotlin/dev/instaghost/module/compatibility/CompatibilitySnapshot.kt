package dev.instaghost.module.compatibility

import android.content.Context
import android.os.Build

/** Minimal host/device snapshot. Avoid storing identifiers or user data. */
data class CompatibilitySnapshot(
    val packageName: String,
    val versionCode: Long,
    val versionName: String,
    val androidApi: Int,
    val sdkRelease: String,
) {
    companion object {
        fun capture(context: Context): CompatibilitySnapshot {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            return CompatibilitySnapshot(
                packageName = context.packageName,
                versionCode = if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong(),
                versionName = info.versionName.orEmpty(),
                androidApi = Build.VERSION.SDK_INT,
                sdkRelease = Build.VERSION.RELEASE.orEmpty(),
            )
        }
    }
}
