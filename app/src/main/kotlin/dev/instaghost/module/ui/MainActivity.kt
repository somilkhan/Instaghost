package dev.instaghost.module.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/** Companion home with a floating, glass-inspired dock and a typography-led layout. */
class MainActivity : Activity() {
    private var selected = 0
    private lateinit var root: LinearLayout
    private lateinit var contentHost: LinearLayout
    private val dark get() = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
    private val bg get() = if (dark) Color.rgb(10,11,13) else Color.rgb(245,246,248)
    private val panel get() = if (dark) Color.rgb(24,25,29) else Color.rgb(255,255,255)
    private val textColor get() = if (dark) Color.rgb(245,246,248) else Color.rgb(20,21,24)
    private val muted get() = if (dark) Color.rgb(159,161,167) else Color.rgb(104,107,115)
    private val stroke get() = if (dark) Color.argb(50,255,255,255) else Color.argb(38,0,0,0)
    private val d get() = resources.displayMetrics.density
    private fun dp(v:Int)=(v*d).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selected = savedInstanceState?.getInt("tab") ?: 0
        build()
    }
    override fun onSaveInstanceState(outState: Bundle) { outState.putInt("tab",selected); super.onSaveInstanceState(outState) }

    private fun build() {
        window.statusBarColor=bg; window.navigationBarColor=bg
        if(Build.VERSION.SDK_INT>=23) window.decorView.systemUiVisibility=if(dark) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(bg) }
        contentHost=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL }
        root.addView(contentHost,LinearLayout.LayoutParams(-1,0,1f))
        setContentView(root); render()
    }
    private fun render() {
        contentHost.removeAllViews()
        val scroll=ScrollView(this).apply { isFillViewport=true; clipToPadding=false }
        val page=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(dp(24),dp(32),dp(24),dp(110)) }
        when(selected) { 0 -> home(page); 1 -> features(page); 2 -> health(page); else -> settings(page) }
        scroll.addView(page); contentHost.addView(scroll,LinearLayout.LayoutParams(-1,-1))
        if(root.childCount>1) root.removeViewAt(1)
        root.addView(floatingDock(),LinearLayout.LayoutParams(-1,-2))
    }
    private fun floatingDock():View {
        val outer=LinearLayout(this).apply { gravity=Gravity.CENTER; setPadding(dp(22),dp(4),dp(22),dp(20)) }
        val dock=LinearLayout(this).apply {
            orientation=LinearLayout.HORIZONTAL; gravity=Gravity.CENTER
            setPadding(dp(7),dp(7),dp(7),dp(7))
            background=GradientDrawable().apply {
                setColor(if(dark) Color.argb(218,34,35,39) else Color.argb(225,255,255,255))
                cornerRadius=dp(40).toFloat(); setStroke(dp(1),stroke)
            }
            elevation=dp(18).toFloat()
        }
        val labels=listOf("Home","Features","Health","Settings")
        val glyph=listOf("⌂","◇","◉","⚙")
        labels.forEachIndexed { i,l ->
            dock.addView(LinearLayout(this).apply {
                orientation=LinearLayout.VERTICAL; gravity=Gravity.CENTER
                setPadding(dp(13),dp(8),dp(13),dp(7))
                background=if(i==selected) GradientDrawable().apply {
                    setColor(if(dark) Color.argb(68,255,255,255) else Color.argb(30,0,0,0)); cornerRadius=dp(30).toFloat()
                } else null
                isClickable=true; isFocusable=true; isSelected=i==selected
                contentDescription=l+" tab"+if(i==selected) ", selected" else ""
                addView(TextView(this@MainActivity).apply { text=glyph[i];textSize=20f;gravity=Gravity.CENTER;setTextColor(textColor);importantForAccessibility=View.IMPORTANT_FOR_ACCESSIBILITY_NO })
                addView(TextView(this@MainActivity).apply { text=l;textSize=11f;gravity=Gravity.CENTER;setTextColor(if(i==selected)textColor else muted);typeface=if(i==selected)Typeface.create("sans-serif-medium",Typeface.NORMAL) else Typeface.DEFAULT;importantForAccessibility=View.IMPORTANT_FOR_ACCESSIBILITY_NO })
                setOnClickListener { selected=i;render() }
            },LinearLayout.LayoutParams(0,-2,1f))
        }
        outer.addView(dock); return outer
    }
    private fun home(p:LinearLayout) {
        p.addView(label("INSTAGHOST   /   EXPERIMENTAL",11,muted,true))
        p.addView(title("Quietly powerful.\nCarefully built.",34).apply { setPadding(0,dp(14),0,dp(12)) })
        p.addView(body("A local LSPosed companion for Instagram. Transparent status, thoughtful controls, and compatibility-first engineering."))
        section(p,"Current state","Infrastructure, not yet a verified feature release")
        p.addView(card { addView(label("INTEGRATION",10,muted,true));addView(title("Runtime bridge pending",20).apply{setPadding(0,dp(7),0,dp(4))});addView(body("The menu scaffold and resolver exist. Runtime event reporting and feature patches still need validation."));addView(chip("Experimental")) })
        section(p,"Getting started",null)
        rowCard(p,"01","Enable module","Enable InstaGhost in LSPosed and scope it to Instagram.")
        rowCard(p,"02","Restart Instagram","Force-stop Instagram and launch it again.")
        rowCard(p,"03","Review status","Health currently reports what is instrumented, not a live log stream.")
    }
    private fun features(p:LinearLayout) {
        p.addView(label("CONTROL CENTER",11,muted,true))
        p.addView(title("Features",32).apply{setPadding(0,dp(12),0,dp(8))})
        p.addView(body("Controls will be available only when implemented and independently validated."))
        section(p,"Not implemented",null)
        listOf("Privacy signal changes" to "Covert manipulation of other people's privacy signals is out of scope.","Feed controls" to "Feed modification is not yet implemented.","Media tools" to "Media utilities are not yet implemented.").forEach { (h,s)->p.addView(card{addView(title(h,17));addView(body(s).apply{setPadding(0,dp(5),0,dp(8))});addView(chip("Planned / unavailable"))}) }
    }
    private fun health(p:LinearLayout) {
        p.addView(label("DIAGNOSTICS",11,muted,true))
        p.addView(title("Health",32).apply{setPadding(0,dp(12),0,dp(8))})
        p.addView(body("Runtime logs are not yet bridged into this screen. No live events are currently available here."))
        section(p,"Hook runtime",null)
        p.addView(card{addView(title("Not instrumented",18));addView(body("Startup and feature-install outcomes are written to sanitized Xposed log categories, but a persistent companion-facing event bridge has not been implemented."))})
        section(p,"Resolver",null)
        p.addView(card{addView(title("Discovery foundation",18));addView(body("DexKit resolver infrastructure exists. Target anchors and feature behavior are not validated against live Instagram builds."))})
        section(p,"Device support",null)
        p.addView(card{addView(title("No verified builds",18));addView(body("Automated build status has not been verified here. Device and Instagram compatibility remains unverified."))})
        section(p,"Privacy",null)
        p.addView(card{addView(title("Sanitized by design",18));addView(body("No message text, account identifiers, cookies or credentials should be captured in diagnostics."))})
        section(p,"Share report",null)
        p.addView(card {
            addView(body("Copy a companion-only report. It does not include Instagram runtime events or device identifiers."))
            addView(TextView(this@MainActivity).apply {
                text="Copy diagnostics report"; textSize=15f; setTextColor(textColor); gravity=Gravity.CENTER
                setPadding(dp(16),dp(14),dp(16),dp(14)); isClickable=true; isFocusable=true
                background=GradientDrawable().apply { setColor(if(dark) Color.rgb(48,49,54) else Color.rgb(235,236,240)); cornerRadius=dp(16).toFloat() }
                setOnClickListener {
                    val report = buildCompanionReport()
                    (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("InstaGhost report", report))
                    Toast.makeText(this@MainActivity,"Report copied",Toast.LENGTH_SHORT).show()
                }
            })
        })
    }
    private fun buildCompanionReport(): String {
        val version = runCatching { packageManager.getPackageInfo(packageName,0).versionName }.getOrNull() ?: "unknown"
        return buildString {
            appendLine("InstaGhost companion report")
            appendLine("Companion version: $version")
            appendLine("Runtime bridge: not connected")
            appendLine("Feature hooks: unverified")
            appendLine("Live Instagram diagnostics: unavailable")
            appendLine("Report scope: companion app only")
        }
    }
    private fun settings(p:LinearLayout) {
        p.addView(label("PREFERENCES",11,muted,true))
        p.addView(title("Settings",32).apply{setPadding(0,dp(12),0,dp(8))})
        section(p,"Appearance","Adaptive monochrome")
        p.addView(card{addView(title("System theme",18));addView(body("Light and dark mode follow Android system settings."))})
        section(p,"Project",null)
        p.addView(card{addView(title("InstaGhost",18));addView(body("Open-source experimental LSPosed module. Unaffiliated with Instagram or Meta."))})
    }
    private fun section(p:LinearLayout,h:String,s:String?) { p.addView(title(h,21).apply{setPadding(0,dp(26),0,dp(7))});if(s!=null)p.addView(label(s,13,muted,false).apply{setPadding(0,0,0,dp(12))}) }
    private fun rowCard(p:LinearLayout,n:String,h:String,s:String) { p.addView(card{val row=LinearLayout(this@MainActivity).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL};row.addView(label(n,12,muted,true),LinearLayout.LayoutParams(dp(42),-2));val c=LinearLayout(this@MainActivity).apply{orientation=LinearLayout.VERTICAL};c.addView(title(h,16));c.addView(body(s).apply{setPadding(0,dp(4),0,0)});row.addView(c,LinearLayout.LayoutParams(0,-2,1f));addView(row)}) }
    private fun card(block:LinearLayout.()->Unit)=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(dp(18),dp(18),dp(18),dp(18));layoutParams=LinearLayout.LayoutParams(-1,-2).apply{bottomMargin=dp(12)};background=GradientDrawable().apply{setColor(panel);cornerRadius=dp(24).toFloat();setStroke(dp(1),stroke)};block()}
    private fun chip(s:String)=TextView(this).apply{text=s;textSize=12f;setTextColor(muted);setPadding(dp(11),dp(7),dp(11),dp(7));background=GradientDrawable().apply{setColor(bg);cornerRadius=dp(40).toFloat();setStroke(dp(1),stroke)};layoutParams=LinearLayout.LayoutParams(-2,-2).apply{topMargin=dp(12)}}
    private fun title(s:String,size:Int)=TextView(this).apply{text=s;textSize=size.toFloat();setTextColor(textColor);typeface=Typeface.create("sans-serif",Typeface.BOLD);letterSpacing=-0.025f}
    private fun body(s:String)=TextView(this).apply{text=s;textSize=15f;setTextColor(muted);setLineSpacing(dp(4).toFloat(),1f)}
    private fun label(s:String,size:Int,c:Int,b:Boolean)=TextView(this).apply{text=s;textSize=size.toFloat();setTextColor(c);if(b)typeface=Typeface.create("sans-serif-medium",Typeface.NORMAL);letterSpacing=if(b)0.08f else 0f}
}
