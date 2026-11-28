package dev.instaghost.module.features

import dev.instaghost.module.core.Resolver

/** Metadata only: these entries do not install hooks or modify Instagram behavior. */
private data class PlannedFeature(
    override val key: String,
    override val title: String,
    override val summary: String,
    override val group: Feature.Group,
) : Feature {
    override fun install(r: Resolver) = Unit
}

object Features {
    val all: List<Feature> = listOf(
        PlannedFeature("ghost_seen", "Hide read receipts", "Planned, not available in this scaffold.", Feature.Group.GHOST),
        PlannedFeature("ghost_typing", "Hide typing", "Planned, not available in this scaffold.", Feature.Group.GHOST),
        PlannedFeature("ghost_story_seen", "Hide story views", "Planned, not available in this scaffold.", Feature.Group.GHOST),
        PlannedFeature("ghost_screenshot", "Hide screenshots", "Planned, not available in this scaffold.", Feature.Group.GHOST),
        PlannedFeature("no_ads", "Remove ads", "Planned, not available in this scaffold.", Feature.Group.FEED),
        PlannedFeature("no_suggested", "Remove suggested people", "Planned, not available in this scaffold.", Feature.Group.FEED),
        PlannedFeature("no_double_tap", "Disable double tap to like", "Planned, not available in this scaffold.", Feature.Group.FEED),
        PlannedFeature("no_tracking_links", "Clean links", "Planned, not available in this scaffold.", Feature.Group.PRIVACY),
    )
}
