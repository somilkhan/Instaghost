package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class PrefsKeyPolicyTest {
    @Test fun knownKeysAreAllowed() {
        assertTrue(PreferenceKey.isAllowed("ui_diagnostics_enabled"))
        assertTrue(PreferenceKey.isAllowed("ui_intro_seen"))
        listOf("ghost_seen", "ghost_typing", "ghost_story_seen", "ghost_screenshot",
            "no_ads", "no_suggested", "no_double_tap", "no_tracking_links")
            .forEach { assertTrue("$it should be allowed", PreferenceKey.isAllowed(it)) }
    }

    @Test fun arbitraryAndSensitiveKeysAreRejected() {
        assertFalse(PreferenceKey.isAllowed("username"))
        assertFalse(PreferenceKey.isAllowed("session_token"))
        assertFalse(PreferenceKey.isAllowed("unknown"))
        assertFalse(PreferenceKey.isAllowed(""))
        assertFalse(PreferenceKey.isAllowed("ui_diagnostics_enabled "))
        assertFalse(PreferenceKey.isAllowed("../prefs"))
    }
}
