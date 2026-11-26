package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class DiagnosticsTest {
    @Test fun missingClassFailuresAreSanitized() {
        assertEquals("CLASS_NOT_FOUND", failureCategory(ClassNotFoundException("private detail")))
        assertEquals("CLASS_NOT_FOUND", failureCategory(NoClassDefFoundError("private detail")))
    }

    @Test fun diagnosticCapacityMustBePositive() {
        try { DiagnosticsLog(0); fail("Expected capacity validation") }
        catch (_: IllegalArgumentException) { }
    }

    @Test fun anchorFingerprintIsStableAndSeparatesAnchorSets() {
        assertEquals(AnchorFingerprint.of("alpha", "beta"), AnchorFingerprint.of("alpha", "beta"))
        assertNotEquals(AnchorFingerprint.of("alpha", "beta"), AnchorFingerprint.of("alpha", "gamma"))
        assertNotEquals(AnchorFingerprint.of("alpha", "beta"), AnchorFingerprint.of("beta", "alpha"))
        assertNotEquals(AnchorFingerprint.of("a", "bc"), AnchorFingerprint.of("ab", "c"))
        assertNotEquals(AnchorFingerprint.of("ab"), AnchorFingerprint.of("a", "b"))
        assertNotEquals(AnchorFingerprint.of("😀"), AnchorFingerprint.of("2:😀"))
    }

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

    @Test fun descriptorRoundTripsPrimitiveParameters() {
        val method = Sample::class.java.getDeclaredMethod("combine", Int::class.javaPrimitiveType, String::class.java)
        assertEquals(method, Descriptor.toMethod(Descriptor.of(method), javaClass.classLoader))
    }

    @Test fun descriptorRoundTripsArrayParameters() {
        val method = Sample::class.java.getDeclaredMethod("arrayArg", Array<String>::class.java)
        assertEquals(method, Descriptor.toMethod(Descriptor.of(method), javaClass.classLoader))
    }

    @Test fun descriptorRoundTripsPrimitiveArrayParameters() {\n        val method = Sample::class.java.getDeclaredMethod("primitiveArrayArg", IntArray::class.java)\n        assertEquals(method, Descriptor.toMethod(Descriptor.of(method), javaClass.classLoader))\n    }\n\n    @Test fun oversizedDescriptorIsRejected() {
        try { Descriptor.toMethod("x".repeat(4097), javaClass.classLoader); fail("Expected rejection") }
        catch (_: IllegalArgumentException) { }
    }

    @Test fun malformedDescriptorIsRejected() {
        try { Descriptor.toMethod("bad", javaClass.classLoader); fail("Expected rejection") }
        catch (_: IllegalArgumentException) { }
    }

    private class Sample { fun combine(value: Int, text: String) = value.toString() + text; fun arrayArg(value: Array<String>) = value.size; fun primitiveArrayArg(value: IntArray) = value.size }

    @Test fun eventRejectsNegativeCountsAndElapsed() {
        try {
            DiagnosticEvent("feature", 1L, RuntimeStatus.FAILED, ResolveOutcome.ERROR, candidateCount = -1)
            fail("Expected candidate count validation")
        } catch (_: IllegalArgumentException) { }
        try {
            DiagnosticEvent("feature", 1L, RuntimeStatus.FAILED, ResolveOutcome.ERROR, elapsedMs = -1)
            fail("Expected elapsed validation")
        } catch (_: IllegalArgumentException) { }
    }

    @Test fun linkageErrorsAreSanitized() {
        assertEquals("LINKAGE_ERROR", failureCategory(LinkageError("private detail")))
    }

    private fun event(key: String) = DiagnosticEvent(
        featureKey = key, appVersionCode = 1L,
        status = RuntimeStatus.UNSUPPORTED, outcome = ResolveOutcome.NO_MATCH
    )
}
