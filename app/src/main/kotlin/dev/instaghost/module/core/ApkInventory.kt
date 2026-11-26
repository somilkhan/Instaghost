package dev.instaghost.module.core

import android.content.pm.ApplicationInfo
import java.io.File

/** Read-only APK inventory. Does not imply DexKit scans split APKs. */
internal object ApkInventory {
    data class Inventory(val base: String?, val splits: List<String>, val missing: Int)

    fun collect(info: ApplicationInfo): Inventory {
        val base = info.sourceDir?.takeIf { it.isNotBlank() }
        val splits = (info.splitSourceDirs ?: emptyArray()).filter { it.isNotBlank() }.distinct()
        val all = listOfNotNull(base) + splits
        val missing = all.count { !File(it).isFile }
        return Inventory(base, splits, missing)
    }
}
