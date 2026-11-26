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

    fun collect(info: ApplicationInfo): Inventory =
        fromPaths(info.sourceDir, info.splitSourceDirs?.asList().orEmpty()) { File(it).isFile }

    /** Pure inventory policy, injectable file-existence check for deterministic tests. */
    internal fun fromPaths(basePath: String?, splitPaths: List<String>, exists: (String) -> Boolean): Inventory {
        val base = basePath?.takeIf { it.isNotBlank() }
        val splits = splitPaths.filter { it.isNotBlank() && it != base }.distinct()
        val all = listOfNotNull(base) + splits
        return Inventory(base, splits, all.count { !exists(it) })
    }
}
