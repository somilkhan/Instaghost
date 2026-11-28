package dev.instaghost.module.core

import java.security.MessageDigest

/** Stable, compact cache discriminator for an ordered resolver anchor set. */
internal object AnchorFingerprint {
    fun of(vararg anchors: String): String = digest(anchors.toList())

    private fun digest(anchors: List<String>): String {
        val canonical = anchors.joinToString(separator = "") { "${it.toByteArray(Charsets.UTF_8).size}:$it" }
        return MessageDigest.getInstance("SHA-256")
            .digest(canonical.toByteArray(Charsets.UTF_8))
            .take(8).joinToString("") { "%02x".format(it) }
    }
}
