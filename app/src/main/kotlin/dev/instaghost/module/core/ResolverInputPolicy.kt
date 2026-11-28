package dev.instaghost.module.core

/** Bounds resolver inputs before they are passed to DexKit. */
internal object ResolverInputPolicy {
    private val keyPattern = Regex("[a-z0-9_]{1,64}")
    const val MAX_ANCHORS = 16
    const val MAX_ANCHOR_LENGTH = 256
    const val MAX_TOTAL_LENGTH = 2048

    fun isValid(key: String, anchors: Array<out String>): Boolean {
        if (!keyPattern.matches(key) || anchors.isEmpty() || anchors.size > MAX_ANCHORS) return false
        if (anchors.any { it.isBlank() || it.length > MAX_ANCHOR_LENGTH }) return false
        if (anchors.toSet().size != anchors.size) return false
        return anchors.sumOf { it.length } <= MAX_TOTAL_LENGTH
    }
}
