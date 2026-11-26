package dev.instaghost.module.ui

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

/** Four-section companion UI with a restrained glass navigation dock. */
class MainActivity : Activity() {
    private var selected = 0
    private lateinit var root: LinearLayout
    private lateinit var contentHost: LinearLayout
    private val dark get() = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES
    private val bg get() = if (dark) Color.rgb(9,10,12) else Color.rgb(246,246,248)
    private val panel get() = if (dark) Color.rgb(23,24,27) else Color.WHITE
    private val textColor get() = if (dark) Color.rgb(245,245,247) else Color.rgb(22,22,24)
    private val muted get() = if (dark) Color.rgb(155,157,164) else Color.rgb(105,107,114)
    private val stroke get() = if (dark) Color.argb(55,255,255,255) else Color.rgb(225,226,230)
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
        if(android.os.Build.VERSION.SDK_INT>=23) window.decorView.systemUiVisibility=if(dark) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        root=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setBackgroundColor(bg) }
        contentHost=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL }
        root.addView(contentHost,LinearLayout.LayoutParams(-1,0,1f))
        root.addView(dock())
        setContentView(root); render()
    }
    private fun dock():View = LinearLayout(this).apply {
        orientation=LinearLayout.HORIZONTAL; gravity=Gravity.CENTER; setPadding(dp(8),dp(8),dp(8),dp(12))
        background=GradientDrawable().apply { setColor(if(dark) Color.argb(230,30,31,35) else Color.argb(235,255,255,255)); cornerRadius=dp(30).toFloat(); setStroke(dp(1),stroke) }
        elevation=dp(12).toFloat()
        val labels=listOf("Home","Features","Health","Settings")
        val glyph=listOf("⌂","◇","◉","⚙")
        labels.forEachIndexed { i,l ->
            addView(LinearLayout(this@MainActivity).apply {
                orientation=LinearLayout.VERTICAL; gravity=Gravity.CENTER
                setPadding(dp(12),dp(7),dp(12),dp(5))
                background=if(i==selected) GradientDrawable().apply { setColor(if(dark) Color.argb(40,255,255,255) else Color.argb(18,0,0,0)); cornerRadius=dp(22).toFloat() } else null
                isClickable = true
                isFocusable = true
                isSelected = i == selected
                contentDescription = l + " tab" + (if (i == selected) ", selected" else "")
                addView(TextView(this@MainActivity).apply { text=glyph[i]; textSize=21f; gravity=Gravity.CENTER; setTextColor(textColor); importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO })
                addView(TextView(this@MainActivity).apply { text=l; textSize=11f; gravity=Gravity.CENTER; setTextColor(if(i==selected) textColor else muted); typeface=if(i==selected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT; importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO })
                setOnClickListener { selected=i; render() }
            },LinearLayout.LayoutParams(0,-2,1f))
        }
    }
    private fun render() {
        contentHost.removeAllViews()
        val scroll=ScrollView(this).apply { isFillViewport=true }
        val page=LinearLayout(this).apply { orientation=LinearLayout.VERTICAL; setPadding(dp(22),dp(26),dp(22),dp(28)) }
        when(selected) {
            0 -> home(page)
            1 -> features(page)
            2 -> health(page)
            else -> settings(page)
        }
        scroll.addView(page); contentHost.addView(scroll,LinearLayout.LayoutParams(-1,-1))
        root.removeViewAt(root.childCount-1); root.addView(dock())
    }
    private fun home(p:LinearLayout) {
        p.addView(label("INSTAGHOST   /   COMPANION",11,muted,true))
        p.addView(title("Less noise.\nMore control.",34).apply { setPadding(0,dp(10),0,dp(8)) })
        p.addView(body("A local companion for your Instagram module. Experimental, transparent, and built to fail safely."))
        p.addView(card(p) {
            addView(label("MODULE STATUS",11,muted,true))
            addView(title("Foundation build",23).apply { setPadding(0,dp(7),0,dp(4)) })
            addView(body("Resolver and diagnostic foundations exist. Feature hooks remain unverified and inactive."))
            addView(chip("0 verified features"))
        })
        section(p,"Quick setup","Three steps to load the module")
        setupRow(p,"01","Enable in LSPosed","Enable InstaGhost and confirm Instagram is in scope.")
        setupRow(p,"02","Restart Instagram","Force-stop the app and open it again.")
        setupRow(p,"03","Check health","Use the Health tab to review validation status.")
        section(p,"Built on a simple promise",null)
        p.addView(body("No account credentials, messages or activity data are collected by this companion UI."))
    }
    private fun features(p:LinearLayout) {
        p.addView(label("CONTROL CENTER",11,muted,true))
        p.addView(title("Features",30).apply { setPadding(0,dp(8),0,dp(5)) })
        p.addView(body("Feature switches appear only when implemented and independently verified."))
        section(p,"Currently unavailable","Planned features are not toggles")
        val list=listOf("Privacy signals" to "Read receipts, typing and story-view modifications are not implemented.","Feed controls" to "Feed filtering and interaction changes are not implemented.","Media tools" to "Download tools are not implemented.")
        list.forEach { (h,s)-> p.addView(card(p) { addView(title(h,17)); addView(body(s).apply{setPadding(0,dp(5),0,0)}); addView(chip("Not available")) }) }
    }
    private fun health(p:LinearLayout) {
        p.addView(label("SYSTEM",11,muted,true)); p.addView(title("Health",30).apply{setPadding(0,dp(8),0,dp(5))})
        p.addView(body("A transparent view of what has and has not been validated."))
        section(p,"Resolver","Dynamic discovery status")
        p.addView(card(p){ addView(title("DexKit foundation",18)); addView(body("Resolver infrastructure is present. Instagram anchors and runtime behavior require build-specific validation.")) })
        section(p,"Compatibility",null)
        p.addView(card(p){ addView(title("Device validation pending",18)); addView(body("No supported Instagram version is claimed until tested on-device.")) })
        section(p,"Diagnostics policy",null)
        p.addView(card(p){ addView(title("Bounded and sanitized",18)); addView(body("Diagnostics use stable failure categories and bounded local events. No free-form account or message content.")) })
    }
    private fun settings(p:LinearLayout) {
        p.addView(label("PREFERENCES",11,muted,true)); p.addView(title("Settings",30).apply{setPadding(0,dp(8),0,dp(5))})
        section(p,"Appearance","Follows your Android system theme")
        p.addView(card(p){addView(title("System appearance",18)); addView(body("Light and dark mode adapt automatically. Theme preference controls the companion UI only."))})
        section(p,"About",null)
        p.addView(card(p){addView(title("InstaGhost",18)); addView(body("Open-source experimental LSPosed companion. Not affiliated with Meta or Instagram."))})
    }
    private fun section(p:LinearLayout,h:String,s:String?) { p.addView(title(h,20).apply{setPadding(0,dp(23),0,dp(5))}); if(s!=null)p.addView(label(s,13,muted,false).apply{setPadding(0,0,0,dp(10))}) }
    private fun setupRow(p:LinearLayout,n:String,h:String,s:String) { p.addView(card(p){val row=LinearLayout(this@MainActivity).apply{orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL};row.addView(label(n,12,muted,true),LinearLayout.LayoutParams(dp(38),-2));val c=LinearLayout(this@MainActivity).apply{orientation=LinearLayout.VERTICAL};c.addView(title(h,16));c.addView(body(s).apply{setPadding(0,dp(4),0,0)});row.addView(c,LinearLayout.LayoutParams(0,-2,1f));addView(row)}) }
    private fun card(p:LinearLayout, block:LinearLayout.()->Unit)=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(dp(17),dp(17),dp(17),dp(17));layoutParams=LinearLayout.LayoutParams(-1,-2).apply{bottomMargin=dp(10)};background=GradientDrawable().apply{setColor(panel);cornerRadius=dp(22).toFloat();setStroke(dp(1),stroke)};block()}
    private fun chip(s:String)=TextView(this).apply{text=s;textSize=12f;setTextColor(muted);setPadding(dp(12),dp(8),dp(12),dp(8));background=GradientDrawable().apply{setColor(bg);cornerRadius=dp(30).toFloat();setStroke(dp(1),stroke)};layoutParams=LinearLayout.LayoutParams(-2,-2).apply{topMargin=dp(12)}}
    private fun title(s:String,size:Int)=TextView(this).apply{text=s;textSize=size.toFloat();setTextColor(textColor);typeface=Typeface.create("sans-serif",Typeface.BOLD)}
    private fun body(s:String)=TextView(this).apply{text=s;textSize=14f;setTextColor(muted);setLineSpacing(dp(3).toFloat(),1f)}
    private fun label(s:String,size:Int,c:Int,b:Boolean)=TextView(this).apply{text=s;textSize=size.toFloat();setTextColor(c);if(b)typeface=Typeface.create("sans-serif-medium",Typeface.BOLD)}
}
