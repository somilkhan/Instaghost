package dev.instaghost.module.core

/** Simple, privacy-conscious diagnostic record. Do not add host payloads or identifiers. */
data class DiagnosticEvent(
    val component: String,
    val status: Status,
    val message: String,
    val timestampMillis: Long = System.currentTimeMillis(),
) {
    enum class Status { INFO, SUCCESS, WARNING, ERROR }

    companion object {
        private val secretPattern = Regex("(?i)(token|password|authorization|cookie)(\\s*[=:]\\s*)([^\\s&,;]+)")
        fun safeMessage(raw: String, maxLength: Int = 240): String {
            val sanitized = secretPattern.replace(raw) { match ->
                match.groupValues[1] + match.groupValues[2] + "[redacted]"
            }.replace(Regex("[\\r\\n\\t]+"), " ").trim()
            return sanitized.take(maxLength.coerceAtLeast(0))
        }
    }
}
