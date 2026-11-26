package dev.instaghost.module.core

import android.content.pm.ApplicationInfo
import java.io.File

/** Read-only APK inventory. Does not imply DexKit scans split APKs. */
internal object ApkInventory {
    data class Inventory(val base: String?, val splits: List<String>, val missing: Int) {
        val totalApks: Int get() = (if (base == null) 0 else 1) + splits.size
        val splitCount: Int get() = splits.size
        val basePresent: Boolean get() = base != null
    }

    fun collect(info: ApplicationInfo): Inventory {
        val base = info.sourceDir?.takeIf { it.isNotBlank() }
        val splits = (info.splitSourceDirs ?: emptyArray()).filter { it.isNotBlank() && it != base }.distinct()
        val all = listOfNotNull(base) + splits
        val missing = all.count { !File(it).isFile }
        return Inventory(base, splits, missing)
    }
}
