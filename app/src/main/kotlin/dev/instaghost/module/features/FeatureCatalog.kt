package dev.instaghost.module.features

/** Presentation-only metadata. UI and diagnostics consume this without needing hook objects. */
data class FeatureDescriptor(
    val key: String,
    val title: String,
    val summary: String,
    val group: Feature.Group,
)

/** Metadata catalog is the single UI-facing source of feature labels and grouping. */
object FeatureCatalog {
    val entries: List<FeatureDescriptor> = Features.all.map {
        FeatureDescriptor(it.key, it.title, it.summary, it.group)
    }

    private val byKey = entries.associateBy { it.key }

    fun find(key: String): FeatureDescriptor? = byKey[key]

    fun search(query: String): List<FeatureDescriptor> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return entries
        return entries.filter {
            it.title.lowercase().contains(q) ||
                it.summary.lowercase().contains(q) ||
                it.key.lowercase().contains(q) ||
                it.group.label.lowercase().contains(q)
        }
    }
}
