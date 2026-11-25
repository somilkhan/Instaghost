package dev.instaghost.module.ui

import android.graphics.*
import android.graphics.drawable.Drawable

/** Tiny line icons drawn in code, so they work inside Instagram without module resources. */
class LineIcon(private val kind: String, private val color: Int) : Drawable() {
    private val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE; strokeWidth = 4f; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND
    }
    override fun draw(c: Canvas) {
        p.color = color
        val s = bounds.width().toFloat(); val x = bounds.left.toFloat(); val y = bounds.top.toFloat()
        val path = Path()
        when (kind) {
            "ghost" -> { path.moveTo(x + s * .2f, y + s * .85f); path.lineTo(x + s * .2f, y + s * .45f)
                path.arcTo(RectF(x + s * .2f, y + s * .12f, x + s * .8f, y + s * .72f), 180f, 180f)
                path.lineTo(x + s * .8f, y + s * .85f); path.lineTo(x + s * .65f, y + s * .73f)
                path.lineTo(x + s * .5f, y + s * .85f); path.lineTo(x + s * .35f, y + s * .73f); path.close() }
            "shield" -> { path.moveTo(x + s * .5f, y + s * .12f); path.lineTo(x + s * .82f, y + s * .25f)
                path.lineTo(x + s * .78f, y + s * .6f); path.lineTo(x + s * .5f, y + s * .88f)
                path.lineTo(x + s * .22f, y + s * .6f); path.lineTo(x + s * .18f, y + s * .25f); path.close() }
            "download" -> { path.moveTo(x + s * .5f, y + s * .15f); path.lineTo(x + s * .5f, y + s * .65f)
                path.moveTo(x + s * .3f, y + s * .47f); path.lineTo(x + s * .5f, y + s * .67f); path.lineTo(x + s * .7f, y + s * .47f)
                path.moveTo(x + s * .2f, y + s * .85f); path.lineTo(x + s * .8f, y + s * .85f) }
            "feed" -> { path.addRoundRect(RectF(x + s * .2f, y + s * .15f, x + s * .8f, y + s * .85f), 8f, 8f, Path.Direction.CW)
                path.moveTo(x + s * .2f, y + s * .55f); path.lineTo(x + s * .8f, y + s * .55f) }
            else -> path.addCircle(x + s * .5f, y + s * .5f, s * .3f, Path.Direction.CW)
        }
        c.drawPath(path, p)
    }
    override fun setAlpha(a: Int) { p.alpha = a }
    override fun setColorFilter(f: ColorFilter?) { p.colorFilter = f }
    @Suppress("OVERRIDE_DEPRECATION") override fun getOpacity() = PixelFormat.TRANSLUCENT
}
