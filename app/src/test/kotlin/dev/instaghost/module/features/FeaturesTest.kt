package dev.instaghost.module.features

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class FeaturesTest {
    @Test fun registryHasUniqueStableKeys() {
        val keys = Features.all.map { it.key }
        assertEquals(keys.size, keys.toSet().size)
        assertTrue(keys.all { it.matches(Regex("[a-z0-9_]{1,64}")) })
    }

    @Test fun scaffoldFeaturesAreAllDisabledAndMetadataOnly() {
        assertTrue(Features.all.isNotEmpty())
        Features.all.forEach { feature ->
            assertFalse("${feature.key} must remain unavailable", feature.implemented)
            assertTrue(feature.title.isNotBlank())
            assertTrue(feature.summary.isNotBlank())
        }
    }
}
