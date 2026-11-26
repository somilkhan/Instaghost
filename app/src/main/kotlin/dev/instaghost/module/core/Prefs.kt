package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/**
 * Module preferences are stored in the target app's process. Keys exposed here
 * are intentionally allow-listed to avoid accidental storage of arbitrary data.
 */
object Prefs {
    private const val FILE = "instaghost"
    private val allowed = setOf("ui_diagnostics_enabled", "ui_intro_seen")
    @Volatile private var sp: SharedPreferences? = null

    fun init(ctx: Context) {
        sp = ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
    }

    private fun store(): SharedPreferences =
        checkNotNull(sp) { "Prefs.init must be called before preferences are accessed" }

    fun bool(key: String, def: Boolean = false): Boolean {
        require(key in allowed) { "Unsupported preference key" }
        return store().getBoolean(key, def)
    }

    fun setBool(key: String, v: Boolean) {
        require(key in allowed) { "Unsupported preference key" }
        store().edit().putBoolean(key, v).apply()
    }

    fun str(key: String): String? {
        require(key in allowed) { "Unsupported preference key" }
        return store().getString(key, null)
    }

    fun setStr(key: String, v: String) {
        require(key in allowed) { "Unsupported preference key" }
        store().edit().putString(key, v).apply()
    }

    fun clear(key: String) {
        require(key in allowed) { "Unsupported preference key" }
        store().edit().remove(key).apply()
    }
}
