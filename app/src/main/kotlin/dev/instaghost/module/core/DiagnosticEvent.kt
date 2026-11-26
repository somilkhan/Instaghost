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
        fun safeMessage(raw: String, maxLength: Int = 240): String {
            val sanitized = raw
                .replace(Regex("(?i)(token|password|authorization|cookie)=\\S+"), "$1=[redacted]")
                .replace(Regex("[\\r\\n\\t]+"), " ")
                .trim()
            return sanitized.take(maxLength)
        }
    }
}
