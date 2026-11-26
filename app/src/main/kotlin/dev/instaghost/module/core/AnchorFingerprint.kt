package dev.instaghost.module.core

import java.security.MessageDigest

/** Stable, compact cache discriminator for an ordered resolver anchor set. */
internal object AnchorFingerprint {
    fun of(anchors: Array<out String>): String = digest(anchors.toList())
    fun of(vararg anchors: String): String = digest(anchors.toList())

    private fun digest(anchors: List<String>): String {
        val canonical = anchors.joinToString(separator = "\u0000")
        return MessageDigest.getInstance("SHA-256")
            .digest(canonical.toByteArray(Charsets.UTF_8))
            .take(8).joinToString("") { "%02x".format(it) }
    }
}
