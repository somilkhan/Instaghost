package dev.instaghost.module.compatibility

/** Stable, display-oriented report. It does not imply hooks were tested or installed. */
data class CompatibilityReport(
    val hostVersionCode: Long,
    val hostVersionName: String,
    val state: CompatibilityState,
    val note: String,
) {
    val headline: String
        get() = when (state) {
            CompatibilityState.SUPPORTED -> "Validated host build"
            CompatibilityState.PARTIAL -> "Partially validated host build"
            CompatibilityState.UNSUPPORTED -> "Unsupported host build"
            CompatibilityState.UNKNOWN -> "Unvalidated host build"
        }

    companion object {
        fun from(snapshot: CompatibilitySnapshot, provider: CompatibilityProvider): CompatibilityReport {
            val profile = provider.profileFor(snapshot.versionCode)
            return CompatibilityReport(
                hostVersionCode = snapshot.versionCode,
                hostVersionName = snapshot.versionName,
                state = profile.state,
                note = profile.notes,
            )
        }
    }
}
