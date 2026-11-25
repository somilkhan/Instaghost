package dev.instaghost.module.ui

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView

/** Companion screen: tells people how to enable the module. Settings live inside Instagram. */
class MainActivity : Activity() {
    override fun onCreate(b: Bundle?) {
        super.onCreate(b)
        val t = Theme(this)
        val d = resources.displayMetrics.density
        fun tv(s: String, size: Float, c: Int, bold: Boolean = false) = TextView(this).apply {
            text = s; textSize = size; setTextColor(c); setPadding(0, 0, 0, (10 * d).toInt())
            if (bold) typeface = Typeface.DEFAULT_BOLD
        }
        window.statusBarColor = Color.TRANSPARENT
        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL; gravity = Gravity.CENTER_VERTICAL
            setBackgroundColor(if (t.dark) Color.rgb(8, 8, 9) else Color.rgb(244, 244, 245))
            setPadding((28 * d).toInt(), 0, (28 * d).toInt(), 0)
            addView(tv("InstaGhost", 34f, t.text, true))
            addView(tv("1. Open LSPosed and enable InstaGhost.", 16f, t.text))
            addView(tv("2. Scope it to Instagram.", 16f, t.text))
            addView(tv("3. Force stop Instagram and open it again.", 16f, t.text))
            addView(tv("4. Long-press the search tab to open the menu.", 16f, t.text))
            addView(tv("Not affiliated with Meta or Instagram.", 12f, t.subtext))
        })
    }
}
