package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class PrefsKeyPolicyTest {
    @Test fun knownKeysAreAllowed() {
        assertTrue(PreferenceKey.isAllowed("ui_diagnostics_enabled"))
        assertTrue(PreferenceKey.isAllowed("ui_intro_seen"))
    }

    @Test fun arbitraryAndSensitiveKeysAreRejected() {
        assertFalse(PreferenceKey.isAllowed("username"))
        assertFalse(PreferenceKey.isAllowed("session_token"))
        assertFalse(PreferenceKey.isAllowed("unknown"))
    }
}
