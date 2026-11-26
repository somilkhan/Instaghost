package dev.instaghost.module.features

import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureStatusTest {
    @Test fun statusStateSetIsStableAndExplicit() {
        assertEquals(
            setOf("NOT_ATTEMPTED", "ATTEMPTED", "INSTALLED", "DISABLED", "UNAVAILABLE", "FAILED"),
            FeatureStatus.State.values().map { it.name }.toSet(),
        )
    }

    @Test fun recordKeepsProvidedValues() {
        val status = FeatureStatus("sample", FeatureStatus.State.FAILED, "test detail", 123L)
        assertEquals("sample", status.key)
        assertEquals(FeatureStatus.State.FAILED, status.state)
        assertEquals("test detail", status.detail)
        assertEquals(123L, status.updatedAtMillis)
    }
}
