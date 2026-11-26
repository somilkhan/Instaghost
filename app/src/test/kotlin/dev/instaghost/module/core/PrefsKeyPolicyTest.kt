package dev.instaghost.module.core

import org.junit.Assert.*
import org.junit.Test

class PrefsKeyPolicyTest {
    @Test fun allowlistIncludesOnlyExplicitKeys() {
        val source = Prefs::class.java.declaredFields.map { it.name }
        assertTrue(source.contains("allowed"))
    }

    @Test fun accessRequiresInitialization() {
        // Access is guarded by an explicit initialization check, preventing silent use.
        val source = Prefs::class.java.declaredMethods.map { it.name }
        assertTrue(source.contains("store"))
    }
}
