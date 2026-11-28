package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ApkInventoryTest {
    @Test fun excludesBlankAndDuplicateSplitPathsAndBaseDuplicates() {
        val inventory = ApkInventory.fromPaths("base.apk", listOf("", "split.apk", "base.apk", "split.apk"), { true })
        assertEquals("base.apk", inventory.base)
        assertEquals(listOf("split.apk"), inventory.splits)
        assertEquals(2, inventory.totalApks)
        assertEquals(1, inventory.splitCount)
    }

    @Test fun countsMissingPathsAndExcludesMissingFilesFromScanInventory() {
        val inventory = ApkInventory.fromPaths("base.apk", listOf("one.apk", "two.apk"), { it == "one.apk" })
        assertFalse(inventory.basePresent)
        assertEquals(3, inventory.totalApks)
        assertEquals(2, inventory.splitCount)
        assertEquals(2, inventory.missing)
    }

    @Test fun blankBaseAndSplitPathsAreIgnored() {
        val inventory = ApkInventory.fromPaths(" ", listOf(" ", "\t", "valid.apk"), { true })
        assertTrue(inventory.base == null)
        assertEquals(listOf("valid.apk"), inventory.splits)
        assertEquals(1, inventory.totalApks)
        assertEquals(0, inventory.missing)
    }

    @Test fun missingBaseDoesNotBecomeScanCandidate() {
        val inventory = ApkInventory.fromPaths("base.apk", listOf("split.apk"), { it == "split.apk" })
        val plan = ApkScanPlanner.plan(inventory)
        assertFalse(plan.usable)
        assertEquals(emptyList<String>(), plan.scanApks)
    }
}