package dev.instaghost.module.features

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FeatureCatalogTest {
    @Test fun featureKeysAreUnique() {
        val keys = FeatureCatalog.entries.map { it.key }
        assertEquals(keys.size, keys.toSet().size)
    }

    @Test fun searchMatchesKeyTitleSummaryAndGroup() {
        assertTrue(FeatureCatalog.search("no_ads").any { it.key == "no_ads" })
        assertTrue(FeatureCatalog.search("sponsored").any { it.key == "no_ads" })
        assertTrue(FeatureCatalog.search("feed").any { it.group == Feature.Group.FEED })
    }

    @Test fun blankQueryReturnsEntireCatalog() {
        assertEquals(FeatureCatalog.entries, FeatureCatalog.search("  "))
    }
}
