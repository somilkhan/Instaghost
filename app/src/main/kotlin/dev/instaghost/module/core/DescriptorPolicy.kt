package dev.instaghost.module.core

/** Structural validation for cached reflection descriptors, before class loading. */
internal object DescriptorPolicy {
    const val MAX_LENGTH = 4096
    const val MAX_PARAMETERS = 128

    fun parse(signature: String): Parts? {
        if (signature.length > MAX_LENGTH) return null
        val pieces = signature.split("#", limit = 3)
        if (pieces.size != 3) return null
        val (className, methodName, parameterText) = pieces
        if (!validName(className) || !validName(methodName, allowDollar = false)) return null
        val parameters = if (parameterText.isEmpty()) emptyList() else parameterText.split(",")
        if (parameters.size > MAX_PARAMETERS || parameters.any { !validType(it) }) return null
        return Parts(className, methodName, parameters)
    }

    private fun validName(value: String, allowDollar: Boolean = true): Boolean =
        value.isNotEmpty() && value.length <= 512 &&
            value.all { it.isLetterOrDigit() || it == '_' || it == '.' || (allowDollar && it == '$') }

    private fun validType(value: String): Boolean {
        if (value.isEmpty() || value.length > 512) return false
        if (value in setOf("boolean", "byte", "char", "short", "int", "long", "float", "double")) return true
        if (value.startsWith("[")) {
            val dimensions = value.takeWhile { it == '[' }.length
            if (dimensions > 8) return false
            val component = value.drop(dimensions)
            if (component.length == 1) return component in "ZBCSIJFD"
            if (component.startsWith("L") && component.endsWith(";")) return validName(component.drop(1).dropLast(1))
            return false
        }
        return validName(value)
    }

    data class Parts(val className: String, val methodName: String, val parameters: List<String>)
}
