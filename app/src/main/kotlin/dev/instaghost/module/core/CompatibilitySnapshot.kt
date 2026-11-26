package dev.instaghost.module.core

import android.content.Context
import android.os.Build

/** Read-only host diagnostics. Avoids assumptions about Instagram internals. */
object CompatibilitySnapshot {
    data class Snapshot(
        val packageName: String,
        val versionName: String?,
        val versionCode: Long,
        val sdk: Int,
        val device: String
    ) {
        fun display(): String = listOf(
            "Package: $packageName",
            "Version: ${versionName ?: "unknown"} ($versionCode)",
            "Android API: $sdk",
            "Device: $device"
        ).joinToString("\n")
    }

    fun read(context: Context): Snapshot {
        val info = context.packageManager.getPackageInfo(context.packageName, 0)
        return Snapshot(context.packageName, info.versionName, info.longVersionCode,
            Build.VERSION.SDK_INT, Build.MANUFACTURER + " " + Build.MODEL)
    }
}
