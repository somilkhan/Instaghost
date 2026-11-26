package dev.instaghost.module.core

import android.content.Context
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method

/**
 * Version-scoped method lookup cache. Callers must validate candidate matches
 * against the target application's current behavior before relying on them.
 */
class Resolver(private val ctx: Context, private val cl: ClassLoader) {
    private val version = ctx.packageManager.getPackageInfo(ctx.packageName, 0).longVersionCode
    private val cache = ctx.getSharedPreferences("instaghost_hooks", Context.MODE_PRIVATE)
    private var bridge: DexKitBridge? = null

    @Synchronized
    fun method(key: String, vararg anchors: String): Method? {
        if (key.isBlank() || anchors.isEmpty() || anchors.any { it.isBlank() }) {
            XposedBridge.log("InstaGhost: invalid resolver request")
            return null
        }
        val fingerprint = anchors.joinToString("\u0000").hashCode().toUInt().toString(16)
        val cacheKey = "$version:$key:$fingerprint"
        cache.getString(cacheKey, null)?.let { signature ->
            val cached = runCatching { Descriptor.toMethod(signature, cl) }.getOrNull()
            if (cached != null) return cached
            cache.edit().remove(cacheKey).apply()
        }
        return runCatching {
            val b = bridge ?: run {
                System.loadLibrary("dexkit")
                DexKitBridge.create(ctx.applicationInfo.sourceDir).also { bridge = it }
            }
            val matches = b.findMethod { matcher { usingStrings(*anchors) } }
            val hit = matches.singleOrNull() ?: return@runCatching null
            hit.getMethodInstance(cl)?.also { cache.edit().putString(cacheKey, Descriptor.of(it)).apply() }
        }.onFailure { XposedBridge.log("InstaGhost: resolver error for '$key': $it") }.getOrNull()
    }

    @Synchronized
    fun close() {
        runCatching { bridge?.close() }.onFailure { XposedBridge.log("InstaGhost: resolver close error: $it") }
        bridge = null
    }
}

object Descriptor {
    fun of(m: Method) = m.declaringClass.name + "#" + m.name + "#" +
        m.parameterTypes.joinToString(",") { it.name }

    fun toMethod(sig: String, cl: ClassLoader): Method {
        require(sig.count { it == '#' } == 2) { "Invalid method descriptor" }
        val (c, n, p) = sig.split("#", limit = 3)
        require(c.isNotBlank() && n.isNotBlank()) { "Invalid method descriptor" }
        val params = if (p.isEmpty()) emptyArray() else p.split(",").map { Class.forName(it, false, cl) }.toTypedArray()
        return Class.forName(c, false, cl).getDeclaredMethod(n, *params).also { it.isAccessible = true }
    }
}
