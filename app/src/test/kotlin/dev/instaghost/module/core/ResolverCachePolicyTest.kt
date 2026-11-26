package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolverCachePolicyTest {
    @Test fun removesOldVersionsAndBoundsCurrentEntries() {
        val keys = (0 until 140).map { "7:key_$it:fingerprint" }.toSet() + "6:old:hash"
        val removed = ResolverCachePolicy.keysToRemove(7L, keys)
        assertEquals(13, removed.size)
        assertTrue("6:old:hash" in removed)
    }

    @Test fun malformedAndOtherVersionKeysAreDiscarded() {
        val keys = setOf("8:valid:hash", "8:malformed", "old:key", ":missing", "9:key:hash")
        assertEquals(setOf("old:key", ":missing", "9:key:hash"),
            ResolverCachePolicy.keysToRemove(8L, keys))
    }

    @Test fun retainsCurrentVersionEntriesWithinLimit() {
        val keys = (0 until 128).map { "8:key_$it:fingerprint" }.toSet()
        assertTrue(ResolverCachePolicy.keysToRemove(8L, keys).isEmpty())
    }
}
