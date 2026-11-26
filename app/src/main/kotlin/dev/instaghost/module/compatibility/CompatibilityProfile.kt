package dev.instaghost.module.compatibility

/** Explicit compatibility states; unknown is the default until validated. */
enum class CompatibilityState { UNKNOWN, PARTIAL, SUPPORTED, UNSUPPORTED }

data class CompatibilityProfile(
    val hostVersionCode: Long,
    val state: CompatibilityState,
    val notes: String = "",
)

/** Injectable lookup so feature/bootstrap code can avoid hardcoding version assumptions. */
interface CompatibilityProvider {
    fun profileFor(hostVersionCode: Long): CompatibilityProfile
}
