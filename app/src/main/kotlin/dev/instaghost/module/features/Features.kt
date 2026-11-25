package dev.instaghost.module.features

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import dev.instaghost.module.core.Prefs
import dev.instaghost.module.core.Resolver

/**
 * Most features are "find a method by string anchor, then neutralize it when the toggle is on".
 * ANCHORS BELOW ARE STARTING POINTS. Verify each one against the current Instagram APK
 * in jadx, then edit. See PLAN.md > "Hook discovery".
 */
private class BlockFeature(
    override val key: String,
    override val title: String,
    override val summary: String,
    override val group: Feature.Group,
    private val anchors: Array<String>,
    private val result: Any? = null,
) : Feature {
    override fun install(r: Resolver) {
        val m = r.method(key, *anchors) ?: return XposedBridge.log("InstaGhost: no target for $key")
        XposedBridge.hookMethod(m, object : XC_MethodHook() {
            override fun beforeHookedMethod(p: MethodHookParam) {
                if (Prefs.bool(key)) p.result = result
            }
        })
    }
}

object Features {
    val all: List<Feature> = listOf(
        // Ghost
        BlockFeature("ghost_seen", "Hide read receipts", "Others will not see that you read their message.",
            Feature.Group.GHOST, arrayOf("TODO_anchor_mark_thread_seen")),
        BlockFeature("ghost_typing", "Hide typing", "Stop sending the typing indicator.",
            Feature.Group.GHOST, arrayOf("TODO_anchor_typing_indicator")),
        BlockFeature("ghost_story_seen", "Hide story views", "View stories without showing up in the viewer list.",
            Feature.Group.GHOST, arrayOf("TODO_anchor_story_seen_state")),
        BlockFeature("ghost_screenshot", "Hide screenshots", "Do not notify on screenshots in disappearing chats.",
            Feature.Group.GHOST, arrayOf("TODO_anchor_screenshot_detect")),
        // Feed
        BlockFeature("no_ads", "Remove ads", "Hide sponsored posts, stories and reels.",
            Feature.Group.FEED, arrayOf("TODO_anchor_is_sponsored"), false),
        BlockFeature("no_suggested", "Remove suggested people", "Hide the 'People you may know' section.",
            Feature.Group.FEED, arrayOf("TODO_anchor_suggested_users")),
        BlockFeature("no_double_tap", "Disable double tap to like", "Stop accidental likes.",
            Feature.Group.FEED, arrayOf("TODO_anchor_double_tap_like")),
        BlockFeature("no_tracking_links", "Clean links", "Remove tracking parameters from shared links.",
            Feature.Group.PRIVACY, arrayOf("TODO_anchor_link_tracking")),
    )
}
