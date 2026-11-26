package dev.instaghost.module.core

/** Limits persisted resolver metadata and removes keys belonging to old app versions. */
internal object ResolverCachePolicy {
    const val MAX_ENTRIES = 128

    fun keysToRemove(currentVersion: Long, keys: Set<String>): Set<String> {
        val versionPrefix = "$currentVersion:"
        val current = keys.filter { it.startsWith(versionPrefix) }.sorted()
        val stale = keys.filterNot { it.startsWith(versionPrefix) }.toSet()
        return stale + current.drop(MAX_ENTRIES)
    }
}
