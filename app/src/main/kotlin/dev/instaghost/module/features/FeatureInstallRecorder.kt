package dev.instaghost.module.features

import android.content.Context
import dev.instaghost.module.core.DiagnosticEvent

/** Stores bounded, non-sensitive feature installation outcomes for troubleshooting. */
class FeatureInstallRecorder(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("instaghost_runtime", Context.MODE_PRIVATE)

    fun record(status: FeatureStatus) {
        val safeDetail = DiagnosticEvent.safeMessage(status.detail)
        prefs.edit()
            .putString("status:${status.key}", status.state.name)
            .putString("detail:${status.key}", safeDetail)
            .putLong("time:${status.key}", status.updatedAtMillis)
            .apply()
    }

    fun status(key: String): FeatureStatus? {
        val state = prefs.getString("status:$key", null)?.let {
            runCatching { FeatureStatus.State.valueOf(it) }.getOrNull()
        } ?: return null
        return FeatureStatus(
            key = key,
            state = state,
            detail = prefs.getString("detail:$key", "").orEmpty(),
            updatedAtMillis = prefs.getLong("time:$key", 0L),
        )
    }
}
