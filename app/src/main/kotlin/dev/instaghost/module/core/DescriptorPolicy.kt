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
        if (!validClassName(className) || !validMethodName(methodName)) return null
        val parameters = if (parameterText.isEmpty()) emptyList() else parameterText.split(",")
        if (parameters.size > MAX_PARAMETERS || parameters.any { !validType(it) }) return null
        return Parts(className, methodName, parameters)
    }

    private fun validClassName(value: String): Boolean =
        value.isNotEmpty() && value.length <= 512 && value.split(".").all(::validIdentifier)

    private fun validMethodName(value: String): Boolean = validIdentifier(value)

    private fun validIdentifier(value: String): Boolean =
        value.isNotEmpty() && value.length <= 512 &&
            (value.first().isLetter() || value.first() == '_' || value.first() == '$') &&
            value.all { it.isLetterOrDigit() || it == '_' || it == '$' }

    private fun validType(value: String): Boolean {
        if (value.isEmpty() || value.length > 512) return false
        if (value in setOf("boolean", "byte", "char", "short", "int", "long", "float", "double")) return true
        if (value.endsWith("[]")) {
            var component = value
            var dimensions = 0
            while (component.endsWith("[]")) { component = component.dropLast(2); dimensions++ }
            if (dimensions > 8) return false
            return validType(component)
        }
        if (value.startsWith("[")) {
            val dimensions = value.takeWhile { it == '[' }.length
            if (dimensions > 8) return false
            val component = value.drop(dimensions)
            if (component.length == 1) return component in "ZBCSIJFD"
            if (component.startsWith("L") && component.endsWith(";"))
                return validClassName(component.drop(1).dropLast(1))
            return false
        }
        return validClassName(value)
    }

    data class Parts(val className: String, val methodName: String, val parameters: List<String>)
}
