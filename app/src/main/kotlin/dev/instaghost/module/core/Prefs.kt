package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/** Stable preference facade. Existing keys/file remain intact for backward compatibility. */
object Prefs {
    private const val FILE = "instaghost"
    private const val SCHEMA_KEY = "_instaghost_schema"
    private const val CURRENT_SCHEMA = 1

    @Volatile private var sp: SharedPreferences? = null

    @Synchronized
    fun init(ctx: Context) {
        val store = ctx.applicationContext.getSharedPreferences(FILE, Context.MODE_PRIVATE)
        migrate(store)
        sp = store
    }

    private fun migrate(store: SharedPreferences) {
        val schema = store.getInt(SCHEMA_KEY, 0)
        if (schema < CURRENT_SCHEMA) {
            // Migration is intentionally additive; feature keys and user choices are preserved.
            store.edit().putInt(SCHEMA_KEY, CURRENT_SCHEMA).apply()
        }
    }

    private fun store(): SharedPreferences =
        checkNotNull(sp) { "Prefs.init(context) must be called before accessing preferences" }

    fun bool(key: String, def: Boolean = false): Boolean = store().getBoolean(key, def)
    fun setBool(key: String, v: Boolean) { store().edit().putBoolean(key, v).apply() }
    fun str(key: String): String? = store().getString(key, null)
    fun setStr(key: String, v: String) { store().edit().putString(key, v).apply() }
}
