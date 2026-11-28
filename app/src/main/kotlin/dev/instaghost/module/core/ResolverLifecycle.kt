package dev.instaghost.module.core

class ResolverLifecycle {
    @Volatile private var closed = false

    fun isOpen(): Boolean = !closed

    fun close(): Boolean {
        if (closed) return false
        synchronized(this) {
            if (closed) return false
            closed = true
            return true
        }
    }
}
