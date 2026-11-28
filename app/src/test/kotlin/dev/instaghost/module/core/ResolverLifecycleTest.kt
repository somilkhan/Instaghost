package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class ResolverLifecycleTest {
    @Test fun closeIsIdempotentAndMakesResolverClosed() {
        val lifecycle = ResolverLifecycle()
        assertTrue(lifecycle.isOpen())
        assertTrue(lifecycle.close())
        assertFalse(lifecycle.isOpen())
        assertFalse(lifecycle.close())
    }
}
