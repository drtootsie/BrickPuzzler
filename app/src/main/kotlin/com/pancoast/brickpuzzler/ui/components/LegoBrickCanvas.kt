package com.pancoast.brickpuzzler.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pancoast.brickpuzzler.model.BrickColor
import com.pancoast.brickpuzzler.model.BrickType

@Composable
fun LegoBrickView(
    brickType: BrickType,
    color: BrickColor,
    studSizeDp: Dp = 36.dp,
    rotation: Int = 0,
    isSelected: Boolean = false,
    alpha: Float = 1.0f,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val studsX = if (rotation == 90) brickType.studsY else brickType.studsX
    val studsY = if (rotation == 90) brickType.studsX else brickType.studsY

    val widthDp = studSizeDp * studsX
    val heightDp = studSizeDp * studsY

    Box(
        modifier = modifier
            .size(widthDp, heightDp)
            .then(
                if (onClick != null) {
                    Modifier.pointerInput(Unit) {
                        detectTapGestures { onClick() }
                    }
                } else Modifier
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawLegoBrick(
                brickType = brickType,
                color = color,
                studsX = studsX,
                studsY = studsY,
                isSelected = isSelected,
                alpha = alpha
            )
        }
    }
}

fun DrawScope.drawLegoBrick(
    brickType: BrickType,
    color: BrickColor,
    studsX: Int,
    studsY: Int,
    isSelected: Boolean,
    alpha: Float = 1.0f
) {
    val w = size.width
    val h = size.height
    val cornerR = 8f

    // Drop shadow
    drawRoundRect(
        color = Color.Black.copy(alpha = 0.25f * alpha),
        topLeft = Offset(4f, 6f),
        size = Size(w, h),
        cornerRadius = CornerRadius(cornerR, cornerR)
    )

    // Base body color
    drawRoundRect(
        color = color.primary.copy(alpha = alpha),
        topLeft = Offset.Zero,
        size = Size(w, h),
        cornerRadius = CornerRadius(cornerR, cornerR)
    )

    // Top bevel highlight (thin bright line on top & left)
    val highlightPath = Path().apply {
        moveTo(cornerR, 2f)
        lineTo(w - cornerR, 2f)
    }
    drawPath(highlightPath, color = color.highlight.copy(alpha = 0.8f * alpha), style = Stroke(width = 4f))

    // Bottom and right edge shadow
    val shadowPath = Path().apply {
        moveTo(w - 2f, cornerR)
        lineTo(w - 2f, h - cornerR)
        lineTo(cornerR, h - 2f)
    }
    drawPath(shadowPath, color = color.shadow.copy(alpha = 0.7f * alpha), style = Stroke(width = 4f))

    // Draw Studs
    val cellW = w / studsX
    val cellH = h / studsY
    val studRadius = (minOf(cellW, cellH) * 0.32f).coerceAtLeast(6f)

    if (brickType.hasEyes) {
        // Draw cartoon friendly eye
        val cx = w / 2f
        val cy = h / 2f
        val eyeRadius = minOf(w, h) * 0.35f

        // White sclera
        drawCircle(Color.White.copy(alpha = alpha), radius = eyeRadius, center = Offset(cx, cy))
        drawCircle(Color.Black.copy(alpha = alpha), radius = eyeRadius, center = Offset(cx, cy), style = Stroke(3f))

        // Big black pupil
        val pupilR = eyeRadius * 0.55f
        drawCircle(Color(0xFF1A1A1A).copy(alpha = alpha), radius = pupilR, center = Offset(cx + 1f, cy))

        // Cute white sparkle glint
        drawCircle(Color.White.copy(alpha = alpha), radius = pupilR * 0.35f, center = Offset(cx - 2f, cy - 2f))
    } else if (brickType.isSlope) {
        // Draw angled slope hatch lines
        val slopeLinePath = Path().apply {
            moveTo(4f, 4f)
            lineTo(w - 4f, h - 4f)
        }
        drawPath(slopeLinePath, color = color.highlight.copy(alpha = 0.5f * alpha), style = Stroke(width = 3f))

        // One stud at bottom
        val studCx = cellW / 2f
        val studCy = cellH / 2f
        drawStud(studCx, studCy, studRadius, color, alpha)
    } else {
        // Standard Lego studs
        for (x in 0 until studsX) {
            for (y in 0 until studsY) {
                val cx = (x + 0.5f) * cellW
                val cy = (y + 0.5f) * cellH
                drawStud(cx, cy, studRadius, color, alpha)
            }
        }
    }

    // If selected, draw glowing outline
    if (isSelected) {
        drawRoundRect(
            color = Color(0xFFFFEB3B),
            topLeft = Offset.Zero,
            size = Size(w, h),
            cornerRadius = CornerRadius(cornerR, cornerR),
            style = Stroke(width = 6f)
        )
    }
}

private fun DrawScope.drawStud(
    cx: Float,
    cy: Float,
    radius: Float,
    color: BrickColor,
    alpha: Float
) {
    // Stud bottom shadow ring
    drawCircle(
        color = color.shadow.copy(alpha = 0.6f * alpha),
        radius = radius,
        center = Offset(cx, cy + 2f)
    )

    // Stud main disk
    drawCircle(
        color = color.primary.copy(alpha = alpha),
        radius = radius,
        center = Offset(cx, cy)
    )

    // Stud top rim highlight
    drawCircle(
        color = color.studHighlight.copy(alpha = 0.9f * alpha),
        radius = radius * 0.75f,
        center = Offset(cx - 1f, cy - 1f),
        style = Stroke(width = 2.5f)
    )
}
