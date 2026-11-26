package dev.instaghost.module

import android.app.Activity
import android.app.Application
import android.view.View
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import dev.instaghost.module.core.LiveLog
import dev.instaghost.module.core.Prefs
import dev.instaghost.module.core.Resolver
import dev.instaghost.module.features.Features
import dev.instaghost.module.ui.GlassMenu

class MainHook : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.instagram.android") return
        XposedHelpers.findAndHookMethod(Application::class.java, "onCreate", object : XC_MethodHook() {
            override fun afterHookedMethod(p: MethodHookParam) {
                val app = p.thisObject as Application
                Prefs.init(app)
                LiveLog.init(app)
                LiveLog.record("Module", LiveLog.Outcome.INFO, "Instagram process initialized")
                val resolver = Resolver(app, lpparam.classLoader)
                Features.all.forEach { f ->
                    runCatching {
                        f.install(resolver)
                        LiveLog.record(f.key, LiveLog.Outcome.INFO, "Install routine completed")
                    }.onFailure { error ->
                        LiveLog.record(f.key, LiveLog.Outcome.FAILED, "Install routine threw " + error.javaClass.simpleName)
                        XposedBridge.log("InstaGhost: " + f.key + " failed: " + error.javaClass.simpleName)
                    }
                }
                resolver.close()
                installMenuEntry()
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
