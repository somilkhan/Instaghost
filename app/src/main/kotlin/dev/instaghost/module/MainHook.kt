package dev.instaghost.module

import android.app.Activity
import android.app.Application
import android.view.View
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import dev.instaghost.module.core.Prefs
import dev.instaghost.module.core.Resolver
import dev.instaghost.module.core.failureCategory
import dev.instaghost.module.features.Features
import dev.instaghost.module.ui.GlassMenu

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != TARGET_PACKAGE) return

        synchronized(registrationLock) {
            if (applicationHookInstalled) return
            applicationHookInstalled = true
        }

        runCatching {
            XposedHelpers.findAndHookMethod(Application::class.java, "onCreate", object : XC_MethodHook() {
                override fun afterHookedMethod(p: MethodHookParam) {
                    val app = p.thisObject as? Application ?: return
                    runCatching { initialize(app, lpparam.classLoader) }
                        .onFailure { XposedBridge.log("InstaGhost: initialization failed (${failureCategory(it)})") }
                }
            })
        }.onFailure {
            synchronized(registrationLock) { applicationHookInstalled = false }
            XposedBridge.log("InstaGhost: startup hook failed (${failureCategory(it)})")
        }
    }

    private fun initialize(app: Application, classLoader: ClassLoader) {
        Prefs.init(app)
        val resolver = runCatching { Resolver(app, classLoader) }.getOrElse {
            XposedBridge.log("InstaGhost: resolver setup failed (${failureCategory(it)})")
            return
        }
        try {
            Features.all.filter { it.implemented }.forEach { feature ->
                runCatching { feature.install(resolver) }
                    .onFailure { XposedBridge.log("InstaGhost: ${feature.key} failed (${failureCategory(it)})") }
            }
        } finally {
            runCatching { resolver.close() }
                .onFailure { XposedBridge.log("InstaGhost: resolver cleanup failed (${failureCategory(it)})") }
        }

        runCatching { installMenuEntry() }
            .onFailure { XposedBridge.log("InstaGhost: menu initialization failed (${failureCategory(it)})") }
    }

    /** Experimental entry: long-press the search tab. The resource ID is not validated. */
    private fun installMenuEntry() {
        synchronized(menuHookLock) {
            if (menuHookInstalled) return
            XposedHelpers.findAndHookMethod(Activity::class.java, "onResume", object : XC_MethodHook() {
                override fun afterHookedMethod(p: MethodHookParam) {
                    val activity = p.thisObject as? Activity ?: return
                    runCatching {
                        val id = activity.resources.getIdentifier("search_tab", "id", activity.packageName)
                        if (id != 0) activity.findViewById<View>(id)?.setOnLongClickListener {
                            GlassMenu.show(activity)
                            true
                        }
                    }.onFailure { XposedBridge.log("InstaGhost: menu entry unavailable (${failureCategory(it)})") }
                }
            })
            menuHookInstalled = true
        }
    }

    companion object {
        private const val TARGET_PACKAGE = "com.instagram.android"
        private val registrationLock = Any()
        private val menuHookLock = Any()
        @Volatile private var applicationHookInstalled = false
        @Volatile private var menuHookInstalled = false
    }
}
