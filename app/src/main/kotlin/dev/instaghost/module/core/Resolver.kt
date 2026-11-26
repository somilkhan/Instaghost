package dev.instaghost.module.core

import android.content.Context
import android.os.Build
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method

/** Version-scoped discovery helper. Cached descriptors are hints and validated on read. */
class Resolver(private val ctx: Context, private val cl: ClassLoader) {
    private val version = ctx.packageManager.getPackageInfo(ctx.packageName, 0).let { info ->
        if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong()
    }
    private val cache = ctx.getSharedPreferences("instaghost_hooks", Context.MODE_PRIVATE)
    private var bridge: DexKitBridge? = null

    init {
        val stale = cache.all.keys.filter { it.substringBefore(":") != version.toString() }
        if (stale.isNotEmpty()) cache.edit().also { editor -> stale.forEach(editor::remove) }.apply()
    }


    fun method(key: String, vararg anchors: String): Method? {
        if (!key.matches(Regex("[a-z0-9_]{1,64}")) || anchors.isEmpty()) return null
        val ck = "$version:$key"
        cache.getString(ck, null)?.let { sig ->
            val cached = runCatching { Descriptor.toMethod(sig, cl) }.getOrNull()
            if (cached != null) return cached
            cache.edit().remove(ck).apply()
        }
        return runCatching {
            val b = bridge ?: run {
                System.loadLibrary("dexkit")
                DexKitBridge.create(ctx.applicationInfo.sourceDir).also { bridge = it }
            }
            val candidates = b.findMethod { matcher { usingStrings(*anchors) } }
            if (candidates.size != 1) return@runCatching null
            candidates.single().getMethodInstance(cl)?.also {
                cache.edit().putString(ck, Descriptor.of(it)).apply()
            }
        }.onFailure { XposedBridge.log("InstaGhost: resolver failure (${failureCategory(it)})") }.getOrNull()
    }

    fun close() { runCatching { bridge?.close() }; bridge = null }
}

object Descriptor {
    private val primitiveTypes = mapOf(
        "boolean" to Boolean::class.javaPrimitiveType, "byte" to Byte::class.javaPrimitiveType,
        "char" to Char::class.javaPrimitiveType, "short" to Short::class.javaPrimitiveType,
        "int" to Int::class.javaPrimitiveType, "long" to Long::class.javaPrimitiveType,
        "float" to Float::class.javaPrimitiveType, "double" to Double::class.javaPrimitiveType,
    )

    fun of(m: Method) = m.declaringClass.name + "#" + m.name + "#" +
        m.parameterTypes.joinToString(",") { it.name }

    fun toMethod(sig: String, cl: ClassLoader): Method {
        require(sig.length <= 4096) { "Descriptor too long" }
        val parts = sig.split("#", limit = 3)
        require(parts.size == 3 && parts[0].isNotBlank() && parts[1].isNotBlank()) { "Malformed descriptor" }
        val (c, n, p) = parts
        val params = if (p.isEmpty()) emptyArray() else p.split(",").map { resolveType(it, cl) }.toTypedArray()
        return Class.forName(c, false, cl).getDeclaredMethod(n, *params).also { it.isAccessible = true }
    }

    private fun resolveType(name: String, cl: ClassLoader): Class<*> {
        primitiveTypes[name]?.let { return requireNotNull(it) }
        return Class.forName(name, false, cl)
    }
}
