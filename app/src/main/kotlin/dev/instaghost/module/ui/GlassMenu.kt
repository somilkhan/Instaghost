package dev.instaghost.module.ui

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import dev.instaghost.module.core.Prefs
import dev.instaghost.module.features.Feature
import dev.instaghost.module.features.Features

object GlassMenu {
    private fun dp(a: Activity, v: Int) = (v * a.resources.displayMetrics.density).toInt()

    fun show(a: Activity) {
        val t = Theme(a)
        val d = Dialog(a)
        d.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            if (Build.VERSION.SDK_INT >= 31) { // real blur of what is behind the dialog
                addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                attributes = attributes.apply { blurBehindRadius = 60 }
                setBackgroundBlurRadius(80)
            }
            setDimAmount(if (t.dark) 0.35f else 0.15f)
        }

        val card = LinearLayout(a).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(a, 20), dp(a, 18), dp(a, 20), dp(a, 22))
            background = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(t.glassTop, t.glass)).apply {
                cornerRadii = floatArrayOf(dp(a, 28).toFloat(), dp(a, 28).toFloat(), dp(a, 28).toFloat(), dp(a, 28).toFloat(), 0f, 0f, 0f, 0f)
                setStroke(dp(a, 1), t.border)
            }
        }
        card.addView(text(a, "InstaGhost", 22f, t.text, true))
        card.addView(text(a, "Experimental scaffold. Features are unavailable until verified and implemented.", 13f, t.subtext, false).apply { setPadding(0, 0, 0, dp(a, 10)) })

        val scroll = ScrollView(a).apply { isVerticalScrollBarEnabled = false }
        val list = LinearLayout(a).apply { orientation = LinearLayout.VERTICAL }
        Feature.Group.values().forEach { g ->
            val items = Features.all.filter { it.group == g }
            if (items.isEmpty()) return@forEach
            list.addView(text(a, g.label.uppercase(), 11f, t.subtext, true).apply { setPadding(0, dp(a, 14), 0, dp(a, 4)) })
            items.forEach { list.addView(row(a, t, it)) }
        }
        scroll.addView(list)
        card.addView(scroll, LinearLayout.LayoutParams(-1, dp(a, 380)))
        d.setContentView(card)
        d.show()
    }

    private fun row(a: Activity, t: Theme, f: Feature): View {
        val icon = ImageView(a).apply {
            setImageDrawable(LineIcon(f.group.name.lowercase().let { if (it == "ghost") "ghost" else if (it == "privacy") "shield" else if (it == "download") "download" else "feed" }, t.text))
        }
        val col = LinearLayout(a).apply {
            orientation = LinearLayout.VERTICAL
            addView(text(a, f.title, 15f, t.text, false))
            addView(text(a, f.summary + if (f.implemented) "" else "  •  Not implemented", 12f, t.subtext, false))
        }
        val sw = Switch(a).apply {
            isChecked = Prefs.bool(f.key) && f.implemented
            isEnabled = f.implemented
            trackTintList = android.content.res.ColorStateList.valueOf(t.track)
            thumbTintList = android.content.res.ColorStateList.valueOf(t.thumbOn)
            setOnCheckedChangeListener { _, on -> Prefs.setBool(f.key, on) }
        }
        return LinearLayout(a).apply {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            setPadding(0, dp(a, 8), 0, dp(a, 8))
            addView(icon, LinearLayout.LayoutParams(dp(a, 24), dp(a, 24)).apply { rightMargin = dp(a, 14) })
            addView(col, LinearLayout.LayoutParams(0, -2, 1f))
            addView(sw)
        }
    }

    private fun text(a: Activity, s: String, size: Float, color: Int, bold: Boolean) = TextView(a).apply {
        text = s; textSize = size; setTextColor(color)
        if (bold) typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
}
