package dev.instaghost.module.core

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ApkScanPlannerTest {
    @Test fun scansBaseOnlyAndFlagsUnscannedSplits() {
        val inventory = ApkInventory.fromPaths("base.apk", listOf("config.arm64.apk", "feature.apk"), { true })
        val plan = ApkScanPlanner.plan(inventory)
        assertEquals(listOf("base.apk"), plan.scanApks)
        assertEquals(2, plan.splitApks.size)
        assertTrue(plan.requiresSplitSupport)
        assertFalse(plan.splitScanningSupported)
        assertTrue(plan.usable)
    }

    @Test fun noBaseMeansNoUsableScanPlan() {
        val inventory = ApkInventory.fromPaths(null, listOf("feature.apk"), { true })
        val plan = ApkScanPlanner.plan(inventory)
        assertTrue(plan.scanApks.isEmpty())
        assertFalse(plan.usable)
        assertTrue(plan.requiresSplitSupport)
    }
}
