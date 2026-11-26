package dev.instaghost.module.core

import android.content.SharedPreferences

/** Explicit, idempotent preference schema migration entry point. */
internal object ConfigMigration {
    private const val SCHEMA_KEY = "_config_schema"
    private const val CURRENT_SCHEMA = 1

    fun run(prefs: SharedPreferences) {
        val current = prefs.getInt(SCHEMA_KEY, 0)
        if (current >= CURRENT_SCHEMA) return
        // Schema 1 preserves existing keys and establishes the schema marker.
        prefs.edit().putInt(SCHEMA_KEY, CURRENT_SCHEMA).apply()
    }
}
