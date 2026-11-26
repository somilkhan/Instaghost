package dev.instaghost.module.compatibility

import org.junit.Assert.assertEquals
import org.junit.Test

class CompatibilityReportTest {
    @Test fun unknownBuildRemainsClearlyUnvalidated() {
        val snapshot = CompatibilitySnapshot("com.instagram.android", 123L, "test", 35, "15")
        val report = CompatibilityReport.from(snapshot, CompatibilityRegistry)
        assertEquals(CompatibilityState.UNKNOWN, report.state)
        assertEquals("Unvalidated host build", report.headline)
    }

    @Test fun headlineReflectsExplicitProfileState() {
        val snapshot = CompatibilitySnapshot("com.instagram.android", 456L, "test", 35, "15")
        val provider = object : CompatibilityProvider {
            override fun profileFor(hostVersionCode: Long) = CompatibilityProfile(
                hostVersionCode, CompatibilityState.PARTIAL, "Some areas unverified"
            )
        }
        val report = CompatibilityReport.from(snapshot, provider)
        assertEquals(CompatibilityState.PARTIAL, report.state)
        assertEquals("Partially validated host build", report.headline)
        assertEquals("Some areas unverified", report.note)
    }
}
