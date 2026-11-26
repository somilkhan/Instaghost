package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class ResolverInputPolicyTest {
    @Test fun acceptsNormalAnchorSets() {
        assertTrue(ResolverInputPolicy.isValid("feed_hook", arrayOf("feed", "suggested")))
    }
    @Test fun rejectsInvalidKeysAndEmptyAnchors() {
        assertFalse(ResolverInputPolicy.isValid("Bad Key", arrayOf("feed")))
        assertFalse(ResolverInputPolicy.isValid("valid_key", emptyArray()))
        assertFalse(ResolverInputPolicy.isValid("valid_key", arrayOf(" ")))
    }
    @Test fun rejectsExcessiveAndOversizedAnchors() {
        assertFalse(ResolverInputPolicy.isValid("key", Array(17) { "a" }))
        assertFalse(ResolverInputPolicy.isValid("key", arrayOf("x".repeat(257))))
        assertFalse(ResolverInputPolicy.isValid("key", arrayOf("x".repeat(1025), "y".repeat(1025))))
    }
    @Test fun rejectsDuplicatesAndAcceptsUpperBounds() {
        assertFalse(ResolverInputPolicy.isValid("key", arrayOf("same", "same")))
        assertTrue(ResolverInputPolicy.isValid("key", Array(8) { ("x".repeat(255) + it.toString()) }))
    }
}
