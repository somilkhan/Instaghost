package dev.instaghost.module.core

/** Limits persisted resolver metadata and removes stale or malformed entries. */
internal object ResolverCachePolicy {
    const val MAX_ENTRIES = 128
    private val cacheKeyPattern = Regex("\\d+:[a-z0-9_]{1,64}:[0-9a-f]{16}")

    fun keysToRemove(currentVersion: Long, keys: Set<String>): Set<String> {
        val currentPrefix = "$currentVersion:"
        val validCurrent = keys.filter { it.startsWith(currentPrefix) && cacheKeyPattern.matches(it) }.sorted()
        val removable = keys - validCurrent.toSet()
        return removable + validCurrent.drop(MAX_ENTRIES)
    }
}
