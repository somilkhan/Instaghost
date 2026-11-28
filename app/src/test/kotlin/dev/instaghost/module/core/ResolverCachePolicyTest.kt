package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolverCachePolicyTest {
    private fun key(version: Int, id: Int) = "$version:key_${id}:${id.toString(16).padStart(16, '0')}"

    @Test fun removesOldVersionsAndBoundsCurrentEntries() {
        val keys = (0 until 140).map { key(7, it) }.toSet() + key(6, 1)
        val removed = ResolverCachePolicy.keysToRemove(7L, keys)
        assertEquals(13, removed.size)
        assertTrue(key(6, 1) in removed)
    }

    @Test fun malformedAndOtherVersionKeysAreDiscarded() {
        val keys = setOf(key(8, 1), "8:malformed", "old:key", ":missing", key(9, 2), "8:key:badfingerprint")
        assertEquals(setOf("8:malformed", "old:key", ":missing", key(9, 2), "8:key:badfingerprint"),
            ResolverCachePolicy.keysToRemove(8L, keys))
    }

    @Test fun retainsCurrentVersionEntriesWithinLimit() {
        val keys = (0 until 128).map { key(8, it) }.toSet()
        assertTrue(ResolverCachePolicy.keysToRemove(8L, keys).isEmpty())
    }
}
