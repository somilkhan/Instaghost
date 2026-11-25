package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences

/** Settings live inside Instagram's own process, so toggles apply without a restart. */
object Prefs {
    private lateinit var sp: SharedPreferences

    fun init(ctx: Context) { sp = ctx.getSharedPreferences("instaghost", Context.MODE_PRIVATE) }
    fun bool(key: String, def: Boolean = false) = sp.getBoolean(key, def)
    fun setBool(key: String, v: Boolean) = sp.edit().putBoolean(key, v).apply()
    fun str(key: String): String? = sp.getString(key, null)
    fun setStr(key: String, v: String) = sp.edit().putString(key, v).apply()
}
