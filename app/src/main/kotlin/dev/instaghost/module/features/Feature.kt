package dev.instaghost.module.features

import dev.instaghost.module.core.Resolver

/** Feature contract. Planned features remain inert until reviewed, implemented and validated. */
interface Feature {
    val key: String
    val title: String
    val summary: String
    val group: Group
    val implemented: Boolean get() = false
    fun install(r: Resolver)

    enum class Group(val label: String) { GHOST("Ghost"), FEED("Feed"), DOWNLOAD("Download"), PRIVACY("Privacy"), ADVANCED("Advanced") }
}
