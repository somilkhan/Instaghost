package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/**
 * Module preferences are stored locally. Only explicitly registered keys are accepted.
 * Feature switches remain registered for planned feature metadata, but are inert until
 * feature implementation and validation are complete.
 */
object PreferenceKey {
    private val allowed = setOf(
        "ui_diagnostics_enabled",
        "ui_intro_seen",
        "ghost_seen",
        "ghost_typing",
        "ghost_story_seen",
        "ghost_screenshot",
        "no_ads",
        "no_suggested",
        "no_double_tap",
        "no_tracking_links",
    )

    fun isAllowed(key: String): Boolean = key in allowed
}

object Prefs {
    private const val FILE = "instaghost"
    @Volatile private var sp: SharedPreferences? = null

    fun init(ctx: Context) {
        sp = ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
    }

    private fun store(): SharedPreferences =
        checkNotNull(sp) { "Prefs.init must be called before preferences are accessed" }

    fun bool(key: String, def: Boolean = false): Boolean {
        require(PreferenceKey.isAllowed(key)) { "Unsupported preference key" }
        return store().getBoolean(key, def)
    }

    fun setBool(key: String, v: Boolean) {
        require(PreferenceKey.isAllowed(key)) { "Unsupported preference key" }
        store().edit().putBoolean(key, v).apply()
    }

    fun str(key: String): String? {
        require(PreferenceKey.isAllowed(key)) { "Unsupported preference key" }
        return store().getString(key, null)
    }

    fun setStr(key: String, v: String) {
        require(PreferenceKey.isAllowed(key)) { "Unsupported preference key" }
        store().edit().putString(key, v).apply()
    }

    fun clear(key: String) {
        require(PreferenceKey.isAllowed(key)) { "Unsupported preference key" }
        store().edit().remove(key).apply()
    }
}
