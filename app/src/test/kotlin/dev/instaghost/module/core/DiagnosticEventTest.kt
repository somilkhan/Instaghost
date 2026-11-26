package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class DiagnosticEventTest {
    @Test fun redactsCommonSecrets() {
        val safe = DiagnosticEvent.safeMessage("token=abc123 password:secret authorization=Bearer cookie=xyz")
        assertFalse(safe.contains("abc123"))
        assertFalse(safe.contains("secret"))
        assertFalse(safe.contains("Bearer"))
        assertFalse(safe.contains("xyz"))
        assertEquals(4, Regex("\\[redacted\\]").findAll(safe).count())
    }

    @Test fun normalizesWhitespaceAndBoundsOutput() {
        assertEquals("line one line two", DiagnosticEvent.safeMessage("line one\\nline two"))
        assertEquals("abc", DiagnosticEvent.safeMessage("abcdef", 3))
        assertEquals("", DiagnosticEvent.safeMessage("abc", -1))
    }
}
