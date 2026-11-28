package dev.instaghost.module.core

import android.content.Context
import android.os.Build
import android.os.SystemClock
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method
import java.lang.reflect.Array
import java.util.concurrent.ConcurrentHashMap

class Resolver(private val ctx: Context, private val cl: ClassLoader) {
    private val version = ctx.packageManager.getPackageInfo(ctx.packageName, 0).let { info ->
        if (Build.VERSION.SDK_INT >= 28) info.longVersionCode else info.versionCode.toLong()
    }
    private val cache = ctx.getSharedPreferences("instaghost_hooks", Context.MODE_PRIVATE)
    private val bridgeLock = Any()
    private var bridge: DexKitBridge? = null
    private val lifecycle = ResolverLifecycle()
    private val resolvedMethods = ConcurrentHashMap<String, Method>()
    private val inventory = ApkInventory.collect(ctx.applicationInfo)
    private val scanPlan = ApkScanPlanner.plan(inventory)

    init {
        XposedBridge.log("InstaGhost: APK inventory base=" + inventory.basePresent + " splits=" + inventory.splitCount + " total=" + inventory.totalApks + " missing=" + inventory.missing + "; scanApks=" + scanPlan.scanApks.size + "; split DEX scan unsupported")
        val removals = ResolverCachePolicy.keysToRemove(version, cache.all.keys)
        if (removals.isNotEmpty()) cache.edit().also { editor -> removals.forEach(editor::remove) }.apply()
    }

    fun method(key: String, vararg anchors: String): Method? {
        if (!lifecycle.isOpen() || !ResolverInputPolicy.isValid(key, anchors)) return null
        val start = SystemClock.elapsedRealtime()
        val ck = version.toString() + ":" + key + ":" + AnchorFingerprint.of(*anchors)
        resolvedMethods[ck]?.let { method ->
            record(key, ResolveOutcome.CACHE_HIT, RuntimeStatus.RESOLVED, elapsed(start), cacheHit=true)
            return method
        }
        cache.getString(ck, null)?.let { sig ->
            val cached = runCatching { Descriptor.toMethod(sig, cl) }.getOrNull()
            if (cached != null) {
                resolvedMethods[ck] = cached
                record(key, ResolveOutcome.CACHE_HIT, RuntimeStatus.RESOLVED, elapsed(start), cacheHit=true)
                return cached
            }
            cache.edit().remove(ck).apply()
            record(key, ResolveOutcome.INVALID_CACHE, RuntimeStatus.RESOLVING, elapsed(start))
        }
        record(key, ResolveOutcome.CACHE_MISS, RuntimeStatus.RESOLVING, elapsed(start))
        return runCatching {
            synchronized(bridgeLock) {
                if (!scanPlan.usable) {
                    record(key, ResolveOutcome.ERROR, RuntimeStatus.UNSUPPORTED, elapsed(start), failureCategory="NO_BASE_APK")
                    return@synchronized null
                }
                val b = bridge ?: run {
                    System.loadLibrary("dexkit")
                    DexKitBridge.create(scanPlan.scanApks.single()).also { bridge = it }
                }
                val candidates = b.findMethod { matcher { usingStrings(*anchors) } }
                if (candidates.size != 1) {
                    val outcome = if (candidates.isEmpty()) ResolveOutcome.NO_MATCH else ResolveOutcome.AMBIGUOUS
                    record(key, outcome, RuntimeStatus.UNSUPPORTED, elapsed(start), candidates.size)
                    return@synchronized null
                }
                candidates.single().getMethodInstance(cl)?.also {
                    resolvedMethods[ck] = it
                    storeDescriptor(ck, Descriptor.of(it))
                    record(key, ResolveOutcome.MATCHED, RuntimeStatus.RESOLVED, elapsed(start), candidates.size)
                } ?: run {
                    record(key, ResolveOutcome.ERROR, RuntimeStatus.FAILED, elapsed(start), candidates.size, failureCategory="METHOD_NOT_FOUND")
                    null
                }
            }
        }.onFailure {
            record(key, ResolveOutcome.ERROR, RuntimeStatus.FAILED, elapsed(start), failureCategory=failureCategory(it))
            XposedBridge.log("InstaGhost: resolver failure (" + failureCategory(it) + ")")
        }.getOrNull()
    }

    private fun storeDescriptor(key: String, descriptor: String) {
        synchronized(bridgeLock) {
            cache.edit().putString(key, descriptor).apply()
            val removals = ResolverCachePolicy.keysToRemove(version, cache.all.keys)
            if (removals.isNotEmpty()) cache.edit().also { editor -> removals.forEach(editor::remove) }.apply()
        }
    }

    private fun elapsed(start: Long) = (SystemClock.elapsedRealtime() - start).coerceAtLeast(0)
    private fun record(key: String, outcome: ResolveOutcome, status: RuntimeStatus, elapsed: Long,
        count: Int? = null, cacheHit: Boolean = false, failureCategory: String? = null) {
        RuntimeDiagnostics.log.add(DiagnosticEvent(key, version, status, outcome, count, elapsed, cacheHit, failureCategory))
    }

    fun close() {
        synchronized(bridgeLock) {
            if (!lifecycle.close()) return
            runCatching { bridge?.close() }
            bridge = null
        }
        resolvedMethods.clear()
    }
}

object Descriptor {
    private val primitiveTypes = mapOf(
        "boolean" to Boolean::class.javaPrimitiveType, "byte" to Byte::class.javaPrimitiveType,
        "char" to Char::class.javaPrimitiveType, "short" to Short::class.javaPrimitiveType,
        "int" to Int::class.javaPrimitiveType, "long" to Long::class.javaPrimitiveType,
        "float" to Float::class.javaPrimitiveType, "double" to Double::class.javaPrimitiveType,
    )
    fun of(m: Method) = m.declaringClass.name + "#" + m.name + "#" + m.parameterTypes.joinToString(",") { it.name }
    fun toMethod(sig: String, cl: ClassLoader): Method {
        val parts = requireNotNull(DescriptorPolicy.parse(sig)) { "Malformed descriptor" }
        val (c, n, p) = parts
        val params = p.map { resolveType(it, cl) }.toTypedArray()
        return Class.forName(c, false, cl).getDeclaredMethod(n, *params).also { it.isAccessible = true }
    }
    private fun resolveType(name: String, cl: ClassLoader): Class<*> {
        primitiveTypes[name]?.let { return requireNotNull(it) }
        var componentName = name
        var dimensions = 0
        while (componentName.endsWith("[]")) { componentName = componentName.dropLast(2); dimensions++ }
        if (dimensions == 0) return Class.forName(name, false, cl)
        require(dimensions <= 8) { "Array dimensions exceed limit" }
        val component = primitiveTypes[componentName]?.let { requireNotNull(it) } ?: Class.forName(componentName, false, cl)
        return Array.newInstance(component, *IntArray(dimensions)).javaClass
    }
}
