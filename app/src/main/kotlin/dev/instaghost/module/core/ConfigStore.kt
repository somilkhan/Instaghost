package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/**
 * Versioned configuration storage independent of host application internals.
 * Use this store for newly introduced settings; existing Prefs remains compatible.
 */
class ConfigStore(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences("instaghost_config", Context.MODE_PRIVATE)

    init { migrate() }

    fun getBoolean(key: String, default: Boolean = false): Boolean = preferences.getBoolean(key, default)
    fun putBoolean(key: String, value: Boolean) { preferences.edit().putBoolean(key, value).apply() }
    fun getString(key: String, default: String? = null): String? = preferences.getString(key, default)

    private fun migrate() {
        val schema = preferences.getInt(SCHEMA_KEY, 0)
        if (schema < 1) preferences.edit().putInt(SCHEMA_KEY, 1).apply()
    }

    companion object { private const val SCHEMA_KEY = "_schema_version" }
}
