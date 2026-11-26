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
import dev.instaghost.module.compatibility.CompatibilitySnapshot
import dev.instaghost.module.core.Prefs
import dev.instaghost.module.features.Feature
import dev.instaghost.module.features.FeatureInstallRecorder
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
            if (Build.VERSION.SDK_INT >= 31) {
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
        card.addView(text(a, "Changes apply right away.", 13f, t.subtext, false).apply { setPadding(0, 0, 0, dp(a, 10)) })

        val search = EditText(a).apply {
            hint = "Search features"
            singleLine = true
            textSize = 14f
            setTextColor(t.text)
            setHintTextColor(t.subtext)
            setPadding(dp(a, 12), dp(a, 8), dp(a, 12), dp(a, 8))
            background = GradientDrawable().apply {
                setColor(t.glass)
                cornerRadius = dp(a, 14).toFloat()
                setStroke(dp(a, 1), t.border)
            }
        }
        card.addView(search, LinearLayout.LayoutParams(-1, dp(a, 46)).apply { bottomMargin = dp(a, 12) })

        val scroll = ScrollView(a).apply { isVerticalScrollBarEnabled = false }
        val list = LinearLayout(a).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(list)
        card.addView(scroll, LinearLayout.LayoutParams(-1, dp(a, 320)))

        val diagnostics = LinearLayout(a).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(a, 12), dp(a, 12), dp(a, 12), dp(a, 12))
            background = GradientDrawable().apply {
                setColor(t.glass)
                cornerRadius = dp(a, 16).toFloat()
                setStroke(dp(a, 1), t.border)
            }
        }
        card.addView(text(a, "Diagnostics", 15f, t.text, true).apply {
            setPadding(0, dp(a, 12), 0, dp(a, 6))
        })
        card.addView(diagnostics)

        fun renderDiagnostics() {
            diagnostics.removeAllViews()
            val recorder = FeatureInstallRecorder(a)
            val counts = Features.all.mapNotNull { recorder.status(it.key) }
                .groupingBy { it.state }.eachCount()
            val attempted = counts[dev.instaghost.module.features.FeatureStatus.State.ATTEMPTED] ?: 0
            val failed = counts[dev.instaghost.module.features.FeatureStatus.State.FAILED] ?: 0
            val unavailable = counts[dev.instaghost.module.features.FeatureStatus.State.UNAVAILABLE] ?: 0
            diagnostics.addView(text(a, "Feature outcomes: $attempted attempted · $failed failed · $unavailable unavailable", 12f, t.subtext, false))
            runCatching { CompatibilitySnapshot.capture(a) }.onSuccess { snapshot ->
                diagnostics.addView(text(a, "Host: ${snapshot.packageName}", 12f, t.subtext, false).apply { setPadding(0, dp(a, 4), 0, 0) })
                diagnostics.addView(text(a, "Version: ${snapshot.versionName} (${snapshot.versionCode})", 12f, t.subtext, false))
                diagnostics.addView(text(a, "Android: ${snapshot.sdkRelease} · API ${snapshot.androidApi}", 12f, t.subtext, false))
            }.onFailure {
                diagnostics.addView(text(a, "Host snapshot unavailable", 12f, t.subtext, false))
            }
            Features.all.forEach { feature ->
                val status = recorder.status(feature.key) ?: return@forEach
                diagnostics.addView(text(a, "${feature.title}: ${status.state.name.lowercase().replace('_', ' ')}", 12f, t.text, false).apply {
                    setPadding(0, dp(a, 5), 0, 0)
                })
            }
        }
        renderDiagnostics()

        fun render(query: String) {
            list.removeAllViews()
            val q = query.trim().lowercase()
            var count = 0
            Feature.Group.values().forEach { g ->
                val items = Features.all.filter { it.group == g && (q.isEmpty() || it.title.lowercase().contains(q) || it.summary.lowercase().contains(q) || it.key.lowercase().contains(q) || g.label.lowercase().contains(q)) }
                if (items.isEmpty()) return@forEach
                count += items.size
                list.addView(text(a, g.label.uppercase(), 11f, t.subtext, true).apply { setPadding(0, dp(a, 14), 0, dp(a, 4)) })
                items.forEach { list.addView(row(a, t, it)) }
            }
            if (count == 0) list.addView(text(a, "No features found.", 14f, t.subtext, false).apply { setPadding(0, dp(a, 20), 0, dp(a, 12)) })
        }
        render("")
        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { render(s?.toString().orEmpty()) }
            override fun afterTextChanged(s: android.text.Editable?) = Unit
        })
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
            addView(text(a, f.summary, 12f, t.subtext, false))
        }
        val sw = Switch(a).apply {
            isChecked = Prefs.bool(f.key)
            trackTintList = android.content.res.ColorStateList.valueOf(t.track)
            thumbTintList = android.content.res.ColorStateList.valueOf(t.text)
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
