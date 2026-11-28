package dev.instaghost.module.core

import android.content.pm.ApplicationInfo
import java.io.File

/** Read-only APK inventory. Only existing APK files are eligible for scanning. */
internal object ApkInventory {
    data class Inventory(val base: String?, val splits: List<String>, val missing: Int) {
        val totalApks: Int get() = (if (base == null) 0 else 1) + splits.size + missing
        val splitCount: Int get() = splits.size
        val basePresent: Boolean get() = base != null
    }

    fun collect(info: ApplicationInfo): Inventory =
        fromPaths(info.sourceDir, info.splitSourceDirs?.asList().orEmpty()) { File(it).isFile }

    /** Pure inventory policy, injectable file-existence check for deterministic tests. */
    internal fun fromPaths(basePath: String?, splitPaths: List<String>, exists: (String) -> Boolean): Inventory {
        val baseCandidate = basePath?.takeIf { it.isNotBlank() }
        val splitCandidates = splitPaths
            .filter { it.isNotBlank() && it != baseCandidate }
            .distinct()
        val allCandidates = listOfNotNull(baseCandidate) + splitCandidates
        val base = baseCandidate?.takeIf(exists)
        val splits = splitCandidates.filter(exists)
        val missing = allCandidates.count { !exists(it) }
        return Inventory(base, splits, missing)
    }
}