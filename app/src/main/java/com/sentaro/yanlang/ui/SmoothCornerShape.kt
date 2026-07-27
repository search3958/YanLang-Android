package com.sentaro.yanlang.ui

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/** A Figma-style smooth squircle shape with a configurable base radius. */
class SmoothCornerShape(private val radius: Dp) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val radiusPx = with(density) { radius.toPx() }
        return Outline.Generic(createSquirclePath(size, radiusPx))
    }

    private fun createSquirclePath(size: Size, cornerRadius: Float): Path {
        val w = size.width
        val h = size.height
        val r = minOf(cornerRadius, w / 2f, h / 2f)
        val lx = minOf(w / 2f, 1.528665f * r)
        val ly = minOf(h / 2f, 1.528665f * r)

        val cx3 = 0.63148f * r
        val cx4 = 0.37282f * r
        val cx5 = 0.16905f * r
        val cx6 = 0.07491f * r
        val cy3 = cx3
        val cy4 = cx4
        val cy5 = cx5
        val cy6 = cx6

        val d1x = 0.04f * r + 0.75697f * (lx - r)
        val d2x = 0.18f * r + 0.90847f * (lx - r)
        val d1y = 0.04f * r + 0.75697f * (ly - r)
        val d2y = 0.18f * r + 0.90847f * (ly - r)

        return Path().apply {
            moveTo(w, h / 2f)
            lineTo(w, h - ly)
            cubicTo(w, h - ly + d1y, w, h - ly + d2y, w - cx6, h - cy3)
            cubicTo(w - cx5, h - cy4, w - cx4, h - cy5, w - cx3, h - cy6)
            cubicTo(w - lx + d2x, h, w - lx + d1x, h, w - lx, h)
            lineTo(lx, h)
            cubicTo(lx - d1x, h, lx - d2x, h, cx3, h - cy6)
            cubicTo(cx4, h - cy5, cx5, h - cy4, cx6, h - cy3)
            cubicTo(0f, h - ly + d2y, 0f, h - ly + d1y, 0f, h - ly)
            lineTo(0f, ly)
            cubicTo(0f, ly - d1y, 0f, ly - d2y, cx6, cy3)
            cubicTo(cx5, cy4, cx4, cy5, cx3, cy6)
            cubicTo(lx - d2x, 0f, lx - d1x, 0f, lx, 0f)
            lineTo(w - lx, 0f)
            cubicTo(w - lx + d1x, 0f, w - lx + d2x, 0f, w - cx3, cy6)
            cubicTo(w - cx4, cy5, w - cx5, cy4, w - cx6, cy3)
            cubicTo(w, ly - d2y, w, ly - d1y, w, ly)
            close()
        }
    }
}
