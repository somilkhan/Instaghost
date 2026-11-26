package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class DiagnosticsTest {
    @Test fun boundedLogRetainsNewestEvents() {
        val log = DiagnosticsLog(2)
        fun event(key:String)=DiagnosticEvent(key,1,RuntimeStatus.RESOLVED,ResolveOutcome.MATCHED)
        log.add(event("one")); log.add(event("two")); log.add(event("three"))
        assertEquals(listOf("two","three"),log.snapshot().map{it.featureKey})
    }
    @Test fun clearRemovesEvents() {
        val log=DiagnosticsLog()
        log.add(DiagnosticEvent("feature",1,RuntimeStatus.FAILED,ResolveOutcome.ERROR))
        log.clear()
        assertTrue(log.snapshot().isEmpty())
    }
    @Test fun sanitizedCategoriesOnly() {
        assertEquals("CLASS_NOT_FOUND",failureCategory(ClassNotFoundException("secret")))
        assertEquals("ACCESS_DENIED",failureCategory(SecurityException("private")))
        assertEquals("UNEXPECTED_ERROR",failureCategory(IllegalStateException("raw message")))
    }
    @Test fun invalidEventDataRejected() {
        try { DiagnosticEvent("invalid key",1,RuntimeStatus.FAILED,ResolveOutcome.ERROR); fail("expected") } catch(_:IllegalArgumentException) {}
        try { DiagnosticEvent("valid",1,RuntimeStatus.FAILED,ResolveOutcome.ERROR,failureCategory="raw exception"); fail("expected") } catch(_:IllegalArgumentException) {}
    }
}
