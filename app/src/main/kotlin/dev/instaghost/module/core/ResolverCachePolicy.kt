package dev.instaghost.module.core

/** Limits persisted resolver metadata and removes keys belonging to old app versions. */
internal object ResolverCachePolicy {
    const val MAX_ENTRIES = 128

    fun keysToRemove(currentVersion: Long, keys: Set<String>): Set<String> {
        val current = keys.filter { it.substringBefore(":") == currentVersion.toString() }.sorted()
        val stale = keys.filter { it.substringBefore(":") != currentVersion.toString() }.toSet()
        return stale + current.drop(MAX_ENTRIES)
    }
}
