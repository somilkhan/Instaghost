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

/** Companion dashboard. Clearly communicates the scaffold's current verified state. */
class MainActivity : Activity() {
    private val dark by lazy { resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK == android.content.res.Configuration.UI_MODE_NIGHT_YES }
    private val bg get() = if (dark) Color.rgb(10, 10, 12) else Color.rgb(246, 246, 248)
    private val panel get() = if (dark) Color.rgb(23, 23, 26) else Color.WHITE
    private val primary get() = if (dark) Color.rgb(245, 245, 247) else Color.rgb(20, 20, 22)
    private val secondary get() = if (dark) Color.rgb(165, 165, 170) else Color.rgb(105, 105, 110)
    private val border get() = if (dark) Color.rgb(48, 48, 52) else Color.rgb(225, 225, 229)
    private val d get() = resources.displayMetrics.density
    private fun dp(v: Int) = (v * d).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            window.decorView.systemUiVisibility = if (dark) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
        }
        val scroll = ScrollView(this).apply { isFillViewport = true }
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(32))
        }
        content.addView(label("INSTAGHOST", 12, secondary, true))
        content.addView(title("A cleaner\nmodule dashboard", 32))
        content.addView(label("Experimental companion app", 14, secondary, false).apply { setPadding(0, dp(8), 0, dp(22)) })
        content.addView(card {
            addView(label("CURRENT STATUS", 11, secondary, true))
            addView(title("Scaffold only", 22).apply { setPadding(0, dp(8), 0, dp(6)) })
            addView(body("The resolver and diagnostics foundation are under development. Feature switches are intentionally unavailable until independently implemented and verified."))
            addView(pill("0 active features"))
        })
        content.addView(section("Setup", "Enable the module through LSPosed"))
        content.addView(step("01", "Enable InstaGhost", "Open LSPosed Manager and enable this module."))
        content.addView(step("02", "Confirm app scope", "Ensure Instagram (com.instagram.android) is selected in the module scope."))
        content.addView(step("03", "Restart Instagram", "Force-stop Instagram, then launch it again to load the module."))
        content.addView(step("04", "Check compatibility", "The in-app entry point is experimental and has not been validated on a real Instagram build."))
        content.addView(section("Feature availability", "All entries are planned, not active"))
        content.addView(card {
            addView(label("NOT IMPLEMENTED", 11, secondary, true))
            addView(body("Feature controls are disabled by design. This build does not alter read receipts, typing status, story views, ads, likes or Instagram tracking behavior."))
        })
        content.addView(section("Diagnostics & privacy", null))
        content.addView(card {
            addView(title("Local-first", 18))
            addView(body("Diagnostics are designed to use bounded, sanitized event categories. This companion screen does not upload account details, messages or activity data."))
        })
        content.addView(label("Not affiliated with Meta or Instagram.", 12, secondary, false).apply { setPadding(0, dp(24), 0, 0) })
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, -1))
        setContentView(root)
    }

    private fun section(head: String, sub: String?) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(0, dp(24), 0, dp(12))
        addView(title(head, 20))
        if (sub != null) addView(label(sub, 13, secondary, false).apply { setPadding(0, dp(4), 0, 0) })
    }

    private fun card(block: LinearLayout.() -> Unit) = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(18), dp(18), dp(18), dp(18))
        background = GradientDrawable().apply {
            setColor(panel); cornerRadius = dp(20).toFloat(); setStroke(dp(1), border)
        }
        block()
    }.also { /* spacing is applied by the parent helper */ }

    private fun step(n: String, heading: String, description: String) = card {
        val row = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        row.addView(label(n, 12, secondary, true), LinearLayout.LayoutParams(dp(38), -2))
        val textCol = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.VERTICAL }
        textCol.addView(title(heading, 16))
        textCol.addView(body(description).apply { setPadding(0, dp(5), 0, 0) })
        row.addView(textCol, LinearLayout.LayoutParams(0, -2, 1f))
        addView(row)
    }

    private fun pill(text: String) = TextView(this).apply {
        this.text = text
        textSize = 12f; setTextColor(secondary); setPadding(dp(12), dp(8), dp(12), dp(8))
        background = GradientDrawable().apply { setColor(bg); cornerRadius = dp(50).toFloat(); setStroke(dp(1), border) }
        setTypeface(Typeface.DEFAULT, Typeface.BOLD)
    }

    private fun title(text: String, size: Int) = TextView(this).apply {
        this.text = text; textSize = size.toFloat(); setTextColor(primary)
        typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }
    private fun body(text: String) = TextView(this).apply {
        this.text = text; textSize = 14f; setTextColor(secondary); lineSpacingExtra = dp(3)
    }
    private fun label(text: String, size: Int, color: Int, bold: Boolean) = TextView(this).apply {
        this.text = text; textSize = size.toFloat(); setTextColor(color)
        if (bold) typeface = Typeface.create("sans-serif-medium", Typeface.BOLD)
    }
}
