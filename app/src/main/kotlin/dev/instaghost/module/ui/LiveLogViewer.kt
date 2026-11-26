package dev.instaghost.module.ui

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import dev.instaghost.module.core.LiveLog\nimport dev.instaghost.module.core.CompatibilitySnapshot
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object LiveLogViewer {
    fun show(activity: Activity) {
        val entries = LiveLog.all()\n        val compatibility = runCatching { CompatibilitySnapshot.read(activity).display() }.getOrDefault("Host details unavailable")
        val content = LinearLayout(activity).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 12, 24, 12) }
        content.addView(TextView(activity).apply { text = compatibility; textSize = 12f; setPadding(0, 0, 0, 16) })\n        if (entries.isEmpty()) {
            content.addView(TextView(activity).apply { text = "No events recorded yet."; textSize = 14f })
        } else entries.forEach { e ->
            val row = LinearLayout(activity).apply { orientation = LinearLayout.VERTICAL; setPadding(0, 10, 0, 10) }
            val heading = TextView(activity).apply {
                text = e.outcome.name + " · " + e.component
                textSize = 13f; setTextColor(when (e.outcome) { LiveLog.Outcome.FAILED -> Color.rgb(210, 70, 70); LiveLog.Outcome.APPLIED -> Color.rgb(70, 160, 100); else -> Color.GRAY })
            }
            row.addView(heading)
            row.addView(TextView(activity).apply { text = e.detail; textSize = 14f })
            row.addView(TextView(activity).apply { text = SimpleDateFormat("dd MMM · HH:mm:ss", Locale.getDefault()).format(Date(e.timestamp)); textSize = 11f; setTextColor(Color.GRAY })
            content.addView(row)
        }
        val scroll = ScrollView(activity).apply { addView(content) }
        AlertDialog.Builder(activity).setTitle("Diagnostics · ${entries.size} events").setView(scroll)
            .setPositiveButton("Close", null)
            .setNeutralButton("Clear") { _, _ -> LiveLog.clear(); show(activity) }
            .show()
    }
}
