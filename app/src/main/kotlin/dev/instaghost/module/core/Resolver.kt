package dev.instaghost.module.core

import android.content.Context
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method

/**
 * Instagram renames classes every release, so we never hardcode names.
 * We search the dex for string anchors, then cache the answer per app version.
 */
class Resolver(private val ctx: Context, private val cl: ClassLoader) {
    private val version = ctx.packageManager.getPackageInfo(ctx.packageName, 0).longVersionCode
    private val cache = ctx.getSharedPreferences("instaghost_hooks", Context.MODE_PRIVATE)
    private var bridge: DexKitBridge? = null

    fun method(key: String, vararg anchors: String): Method? {
        val ck = "$version:$key"
        cache.getString(ck, null)?.let { sig ->
            runCatching { return Descriptor.toMethod(sig, cl) }
        }
        return runCatching {
            val b = bridge ?: run {
                System.loadLibrary("dexkit")
                DexKitBridge.create(ctx.applicationInfo.sourceDir).also { bridge = it }
            }
            val hit = b.findMethod { matcher { usingStrings(*anchors) } }.singleOrNull()
            hit?.getMethodInstance(cl)?.also { cache.edit().putString(ck, Descriptor.of(it)).apply() }
        }.onFailure { XposedBridge.log("InstaGhost: resolve '$key' failed: $it") }.getOrNull()
    }

    fun close() { bridge?.close(); bridge = null }
}

object Descriptor {
    fun of(m: Method) = m.declaringClass.name + "#" + m.name + "#" +
        m.parameterTypes.joinToString(",") { it.name }

    fun toMethod(sig: String, cl: ClassLoader): Method {
        val (c, n, p) = sig.split("#", limit = 3)
        val params = if (p.isEmpty()) emptyArray() else p.split(",").map { Class.forName(it, false, cl) }.toTypedArray()
        return Class.forName(c, false, cl).getDeclaredMethod(n, *params).also { it.isAccessible = true }
    }
}
