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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pancoast.brickpuzzler.model.BlueprintSlot
import com.pancoast.brickpuzzler.model.PlacedBrick

@Composable
fun LegoBaseplateView(
    cols: Int,
    rows: Int,
    cellSizeDp: Dp = 38.dp,
    placedBricks: List<PlacedBrick>,
    blueprintSlots: List<BlueprintSlot> = emptyList(),
    baseplateColor: Color = Color(0xFF2E7D32), // Classic Lego Green
    onCellClicked: ((x: Int, y: Int) -> Unit)? = null,
    onBrickClicked: ((PlacedBrick) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val widthDp = cellSizeDp * cols
    val heightDp = cellSizeDp * rows

    Box(
        modifier = modifier
            .size(widthDp, heightDp)
            .then(
                if (onCellClicked != null) {
                    Modifier.pointerInput(cols, rows) {
                        detectTapGestures { offset ->
                            val cellPx = size.width / cols
                            val cellPy = size.height / rows
                            val x = (offset.x / cellPx).toInt().coerceIn(0, cols - 1)
                            val y = (offset.y / cellPy).toInt().coerceIn(0, rows - 1)

                            // Check if a placed brick was tapped
                            val clickedBrick = placedBricks.find { brick ->
                                x >= brick.gridX && x < brick.gridX + brick.actualWidth &&
                                y >= brick.gridY && y < brick.gridY + brick.actualHeight
                            }

                            if (clickedBrick != null && onBrickClicked != null) {
                                onBrickClicked(clickedBrick)
                            } else {
                                onCellClicked(x, y)
                            }
                        }
                    }
                } else Modifier
            )
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cellW = size.width / cols
            val cellH = size.height / rows

            // 1. Draw baseplate background
            drawRoundRect(
                color = baseplateColor,
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(12f, 12f)
            )

            // Outer bevel
            drawRoundRect(
                color = Color.White.copy(alpha = 0.2f),
                topLeft = Offset.Zero,
                size = size,
                cornerRadius = CornerRadius(12f, 12f),
                style = Stroke(width = 4f)
            )

            // 2. Baseplate studs
            val studRadius = (minOf(cellW, cellH) * 0.22f).coerceAtLeast(4f)
            for (r in 0 until rows) {
                for (c in 0 until cols) {
                    val cx = (c + 0.5f) * cellW
                    val cy = (r + 0.5f) * cellH

                    // Stud shadow ring
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.2f),
                        radius = studRadius,
                        center = Offset(cx, cy + 1f)
                    )
                    // Baseplate stud dot
                    drawCircle(
                        color = baseplateColor.copy(alpha = 0.9f),
                        radius = studRadius,
                        center = Offset(cx, cy)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.25f),
                        radius = studRadius * 0.7f,
                        center = Offset(cx - 0.5f, cy - 0.5f),
                        style = Stroke(width = 1.5f)
                    )
                }
            }

            // 3. Draw Blueprint Ghost Slots
            val dashedStroke = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
            )

            for (slot in blueprintSlots) {
                val isOccupied = placedBricks.any {
                    it.gridX == slot.targetGridX && it.gridY == slot.targetGridY && it.brickType == slot.brickType
                }
                if (!isOccupied) {
                    val slotStudsX = if (slot.rotation == 90) slot.brickType.studsY else slot.brickType.studsX
                    val slotStudsY = if (slot.rotation == 90) slot.brickType.studsX else slot.brickType.studsY
                    val slotTopLeft = Offset(slot.targetGridX * cellW, slot.targetGridY * cellH)
                    val slotBoxSize = Size((slotStudsX * cellW) - 4f, (slotStudsY * cellH) - 4f)

                    // Fill slot with translucent color
                    drawRoundRect(
                        color = slot.requiredColor.primary.copy(alpha = 0.22f),
                        topLeft = slotTopLeft + Offset(2f, 2f),
                        size = slotBoxSize,
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                    // Dashed guide border
                    drawRoundRect(
                        color = slot.requiredColor.primary.copy(alpha = 0.85f),
                        topLeft = slotTopLeft + Offset(2f, 2f),
                        size = slotBoxSize,
                        cornerRadius = CornerRadius(6f, 6f),
                        style = dashedStroke
                    )
                }
            }

            // 4. Draw Placed Bricks
            for (brick in placedBricks) {
                val bStudsX = brick.actualWidth
                val bStudsY = brick.actualHeight
                val brickOffset = Offset(brick.gridX * cellW, brick.gridY * cellH)
                val brickPixelSize = Size(bStudsX * cellW, bStudsY * cellH)

                // Render brick within clip
                drawContext.canvas.save()
                drawContext.canvas.translate(brickOffset.x, brickOffset.y)

                drawLegoBrick(
                    brickType = brick.brickType,
                    color = brick.color,
                    studsX = bStudsX,
                    studsY = bStudsY,
                    isSelected = false
                )

                drawContext.canvas.restore()
            }
        }
    }
}
