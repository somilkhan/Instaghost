package dev.instaghost.module.core

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

/**
 * Small bounded diagnostic journal for module events.
 * Never store message contents, account identifiers or raw exception messages.
 */
object LiveLog {
    private const val PREFS = "instaghost_diagnostics"
    private const val KEY_EVENTS = "events"
    private const val LIMIT = 250
    private lateinit var prefs: SharedPreferences

    enum class Outcome { APPLIED, FAILED, SKIPPED, INFO }

    data class Event(
        val timestamp: Long,
        val component: String,
        val outcome: Outcome,
        val detail: String
    )

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
    }

    @Synchronized
    fun record(component: String, outcome: Outcome, detail: String) {
        if (!::prefs.isInitialized) return
        val events = read().toMutableList()
        events.add(Event(System.currentTimeMillis(), component.take(80), outcome, sanitize(detail).take(240)))
        while (events.size > LIMIT) events.removeAt(0)
        val array = JSONArray()
        events.forEach { event ->
            array.put(JSONObject().put("timestamp", event.timestamp)
                .put("component", event.component)
                .put("outcome", event.outcome.name)
                .put("detail", event.detail))
        }
        prefs.edit().putString(KEY_EVENTS, array.toString()).apply()
    }

    fun all(): List<Event> = if (!::prefs.isInitialized) emptyList() else read().asReversed()

    @Synchronized
    fun clear() {
        if (::prefs.isInitialized) prefs.edit().remove(KEY_EVENTS).apply()
    }

    private fun read(): List<Event> {
        val raw = prefs.getString(KEY_EVENTS, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).map { i ->
                val obj = array.getJSONObject(i)
                Event(obj.optLong("timestamp"), obj.optString("component"),
                    runCatching { Outcome.valueOf(obj.optString("outcome")) }.getOrDefault(Outcome.INFO),
                    obj.optString("detail"))
            }
        }.getOrDefault(emptyList())
    }

    private fun sanitize(value: String): String {
        // Avoid persistence of likely account/user data or stack traces.
        return value.replace(Regex("(?i)(password|token|cookie|authorization)\\s*[:=]\\s*[^\\s,;]+"), "$1=[redacted]")
            .replace(Regex("\\b[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}\\b"), "[email]")
            .replace(Regex("\\b\\d{5,}\\b"), "[number]")
            .replace(Regex("[\\r\\n\\t]+"), " ")
    }
}
