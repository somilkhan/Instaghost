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
import dev.instaghost.module.features.FeatureStatus
import dev.instaghost.module.features.FeatureInstallRecorder
import dev.instaghost.module.features.Features
import dev.instaghost.module.ui.GlassMenu

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.instagram.android") return

        XposedHelpers.findAndHookMethod(Application::class.java, "onCreate", object : XC_MethodHook() {
            override fun afterHookedMethod(p: MethodHookParam) {
                val app = p.thisObject as Application
                runCatching {
                    Prefs.init(app)
                    val recorder = FeatureInstallRecorder(app)
                    val resolver = Resolver(app, lpparam.classLoader)
                    try {
                        Features.all.forEach { feature ->
                            // Privacy-protection bypass features are deliberately not installed.
                            if (feature.key.startsWith("ghost_")) {
                                recorder.record(FeatureStatus(feature.key, FeatureStatus.State.UNAVAILABLE,
                                    "Not implemented in this build"))
                                return@forEach
                            }
                            try {
                                feature.install(resolver)
                                recorder.record(FeatureStatus(feature.key, FeatureStatus.State.ATTEMPTED,
                                    "Installation call completed; target resolution is not independently confirmed"))
                            } catch (e: Throwable) {
                                recorder.record(FeatureStatus(feature.key, FeatureStatus.State.FAILED,
                                    "Install error: ${e.javaClass.simpleName}"))
                                XposedBridge.log("InstaGhost: ${feature.key} failed: $e")
                            }
                        }
                    } finally {
                        resolver.close()
                    }
                    installMenuEntry()
                }.onFailure { XposedBridge.log("InstaGhost: initialization failed: $it") }
            }
        })
    }

    /** Long-press the search tab to open the InstaGhost menu. */
    private fun installMenuEntry() {
        XposedHelpers.findAndHookMethod(Activity::class.java, "onResume", object : XC_MethodHook() {
            override fun afterHookedMethod(p: MethodHookParam) {
                val a = p.thisObject as Activity
                val id = a.resources.getIdentifier("search_tab", "id", a.packageName)
                if (id == 0) return
                a.findViewById<View>(id)?.setOnLongClickListener { GlassMenu.show(a); true }
            }
        })
    }
}
