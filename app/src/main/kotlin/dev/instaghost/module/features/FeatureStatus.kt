package dev.instaghost.module.features

/** Runtime outcome kept separate from feature implementation and settings UI. */
data class FeatureStatus(
    val key: String,
    val state: State,
    val detail: String = "",
    val updatedAtMillis: Long = System.currentTimeMillis(),
) {
    enum class State { NOT_ATTEMPTED, ATTEMPTED, INSTALLED, DISABLED, UNAVAILABLE, FAILED }
}
