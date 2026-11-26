package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class DiagnosticsTest {
    @Test fun boundedBufferDropsOldest() {
        val log = DiagnosticsLog(2)
        log.add(event("one"))
        log.add(event("two"))
        log.add(event("three"))
        assertEquals(listOf("two", "three"), log.snapshot().map { it.featureKey })
    }

    @Test fun clearRemovesEvents() {
        val log = DiagnosticsLog()
        log.add(event("feature"))
        log.clear()
        assertTrue(log.snapshot().isEmpty())
    }

    @Test fun eventRejectsUnsafeFeatureIdentifiers() {
        try {
            event("../account")
            fail("Expected validation to reject identifier")
        } catch (_: IllegalArgumentException) { }
    }

    @Test fun exceptionCategoriesAreStableAndSafe() {
        assertEquals("METHOD_NOT_FOUND", failureCategory(NoSuchMethodException("private text")))
        assertEquals("ACCESS_DENIED", failureCategory(SecurityException("private text")))
        assertEquals("UNEXPECTED_ERROR", failureCategory(IllegalStateException("private text")))
    }

    private fun event(key: String) = DiagnosticEvent(
        featureKey = key, appVersionCode = 1L,
        status = RuntimeStatus.UNSUPPORTED, outcome = ResolveOutcome.NO_MATCH
    )
}
