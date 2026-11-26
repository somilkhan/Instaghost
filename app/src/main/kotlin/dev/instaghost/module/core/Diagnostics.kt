package dev.instaghost.module.core

enum class RuntimeStatus { DISABLED, RESOLVING, RESOLVED, INSTALLED, UNSUPPORTED, FAILED }
enum class ResolveOutcome { CACHE_HIT, CACHE_MISS, MATCHED, NO_MATCH, AMBIGUOUS, INVALID_CACHE, ERROR }

data class DiagnosticEvent(
    val featureKey: String, val appVersionCode: Long, val status: RuntimeStatus,
    val outcome: ResolveOutcome, val candidateCount: Int? = null, val elapsedMs: Long = 0,
    val cacheHit: Boolean = false, val failureCategory: String? = null,
) {
    init {
        require(featureKey.matches(Regex("[a-z0-9_]{1,64}")))
        require(candidateCount == null || candidateCount >= 0)
        require(elapsedMs >= 0)
        require(failureCategory == null || failureCategory.matches(Regex("[A-Z_]{1,40}")))
    }
}
class DiagnosticsLog(private val capacity: Int = 100) {
    init { require(capacity > 0) }
    private val events = ArrayDeque<DiagnosticEvent>()
    @Synchronized fun add(event: DiagnosticEvent) { events.addLast(event); while(events.size>capacity) events.removeFirst() }
    @Synchronized fun snapshot(): List<DiagnosticEvent> = events.toList()
    @Synchronized fun clear() = events.clear()
}
object RuntimeDiagnostics { val log = DiagnosticsLog(100) }
fun failureCategory(error: Throwable): String = when (error) {
    is ClassNotFoundException, is NoClassDefFoundError -> "CLASS_NOT_FOUND"
    is NoSuchMethodException -> "METHOD_NOT_FOUND"
    is SecurityException -> "ACCESS_DENIED"
    is LinkageError -> "LINKAGE_ERROR"
    else -> "UNEXPECTED_ERROR"
}
