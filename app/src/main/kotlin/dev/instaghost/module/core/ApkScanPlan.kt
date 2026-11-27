package dev.instaghost.module.core

internal data class ApkScanPlan(
    val baseApk: String?,
    val splitApks: List<String>,
    val scanApks: List<String>,
    val splitScanningSupported: Boolean,
) {
    val requiresSplitSupport: Boolean get() = splitApks.isNotEmpty()
    val usable: Boolean get() = scanApks.isNotEmpty()
}

internal object ApkScanPlanner {
    fun plan(inventory: ApkInventory.Inventory): ApkScanPlan =
        ApkScanPlan(
            baseApk = inventory.base,
            splitApks = inventory.splits,
            scanApks = listOfNotNull(inventory.base),
            splitScanningSupported = false,
        )
}
