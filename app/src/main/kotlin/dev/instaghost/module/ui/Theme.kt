package dev.instaghost.module.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color

/** Monochrome only. No accent colors. Glass comes from blur, thin borders and soft alpha. */
class Theme(ctx: Context) {
    val dark = ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    val glass: Int = if (dark) Color.argb(150, 18, 18, 20) else Color.argb(150, 250, 250, 250)
    val glassTop: Int = if (dark) Color.argb(40, 255, 255, 255) else Color.argb(200, 255, 255, 255)
    val border: Int = if (dark) Color.argb(60, 255, 255, 255) else Color.argb(70, 0, 0, 0)
    val text: Int = if (dark) Color.rgb(242, 242, 242) else Color.rgb(14, 14, 14)
    val subtext: Int = if (dark) Color.rgb(160, 160, 164) else Color.rgb(96, 96, 100)
    val track: Int = if (dark) Color.argb(70, 255, 255, 255) else Color.argb(60, 0, 0, 0)
    val thumbOn: Int = text
}
