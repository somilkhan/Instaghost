package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/** Settings live inside Instagram's own process, so toggles apply without a restart. */
object Prefs {
    private var sp: SharedPreferences? = null

    @Synchronized
    fun init(ctx: Context) {
        sp = ctx.applicationContext.getSharedPreferences("instaghost", Context.MODE_PRIVATE)
    }

    private fun store(): SharedPreferences =
        checkNotNull(sp) { "Prefs.init(context) must be called before accessing preferences" }

    fun bool(key: String, def: Boolean = false): Boolean = store().getBoolean(key, def)
    fun setBool(key: String, v: Boolean) { store().edit().putBoolean(key, v).apply() }
    fun str(key: String): String? = store().getString(key, null)
    fun setStr(key: String, v: String) { store().edit().putString(key, v).apply() }
}
