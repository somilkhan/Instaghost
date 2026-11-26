package dev.instaghost.module.features

import dev.instaghost.module.core.Prefs

/** Centralized feature toggle API. Storage keys intentionally match the legacy feature keys. */
object FeatureSettings {
    fun isEnabled(key: String, default: Boolean = false): Boolean = Prefs.bool(key, default)

    fun setEnabled(key: String, enabled: Boolean) = Prefs.setBool(key, enabled)

    fun toggle(key: String): Boolean {
        val next = !isEnabled(key)
        setEnabled(key, next)
        return next
    }
}
