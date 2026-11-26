package dev.instaghost.module.compatibility

/** Registry intentionally starts empty: versions are only marked supported after validation. */
object CompatibilityRegistry : CompatibilityProvider {
    private val profiles: Map<Long, CompatibilityProfile> = emptyMap()

    override fun profileFor(hostVersionCode: Long): CompatibilityProfile =
        profiles[hostVersionCode] ?: CompatibilityProfile(
            hostVersionCode = hostVersionCode,
            state = CompatibilityState.UNKNOWN,
            notes = "No validation record for this host build.",
        )
}
