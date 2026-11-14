package com.pancoast.brickpuzzler.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pancoast.brickpuzzler.model.BrickColor
import com.pancoast.brickpuzzler.model.BrickType
import com.pancoast.brickpuzzler.model.PlacedBrick
import com.pancoast.brickpuzzler.ui.components.CelebrationOverlay
import com.pancoast.brickpuzzler.ui.components.LegoBaseplateView
import com.pancoast.brickpuzzler.ui.components.LegoBrickView
import com.pancoast.brickpuzzler.util.SoundHapticManager
import com.pancoast.brickpuzzler.util.SpeechManager
import com.pancoast.brickpuzzler.util.StarlightManager

@Composable
fun FreeBuildScreen(
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager,
    onBack: () -> Unit
) {
    val placedBricks = remember { mutableStateListOf<PlacedBrick>() }
    var selectedType by remember { mutableStateOf(BrickType.B2x2) }
    var selectedColor by remember { mutableStateOf(BrickColor.RED) }
    var rotation by remember { mutableIntStateOf(0) }
    var showCelebration by remember { mutableStateOf(false) }

    val stars by starlightManager.stars.collectAsState()

    val availableTypes = listOf(
        BrickType.B1x1,
        BrickType.B1x2,
        BrickType.B1x3,
        BrickType.B1x4,
        BrickType.B2x2,
        BrickType.B2x4,
        BrickType.SLOPE_2x2,
        BrickType.EYE_1x1
    )

    val availableColors = BrickColor.entries.toList()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111827))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1F2937)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🎨", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Free Build Sandbox",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Undo button
                    IconButton(
                        onClick = {
                            if (placedBricks.isNotEmpty()) {
                                soundHapticManager.playPopSound()
                                placedBricks.removeAt(placedBricks.lastIndex)
                            }
                        },
                        enabled = placedBricks.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Undo,
                            contentDescription = "Undo",
                            tint = if (placedBricks.isNotEmpty()) Color.White else Color.DarkGray
                        )
                    }

                    // Clear button
                    IconButton(
                        onClick = {
                            if (placedBricks.isNotEmpty()) {
                                soundHapticManager.playPopSound()
                                placedBricks.clear()
                            }
                        },
                        enabled = placedBricks.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Clear",
                            tint = if (placedBricks.isNotEmpty()) Color(0xFFFF5252) else Color.DarkGray
                        )
                    }
                }
            }

            // Quick Color Palette Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                availableColors.forEach { col ->
                    val isColSelected = col == selectedColor
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(col.primary)
                            .border(
                                width = if (isColSelected) 3.dp else 1.dp,
                                color = if (isColSelected) Color.White else Color.Black.copy(alpha = 0.4f),
                                shape = CircleShape
                            )
                            .clickable {
                                soundHapticManager.playPickupSound()
                                selectedColor = col
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Baseplate Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LegoBaseplateView(
                    cols = 10,
                    rows = 10,
                    cellSizeDp = 34.dp,
                    placedBricks = placedBricks,
                    baseplateColor = Color(0xFF1B5E20), // Dark green baseplate
                    onCellClicked = { x, y ->
                        // Place selected brick
                        soundHapticManager.playSnapSound()
                        placedBricks.add(
                            PlacedBrick(
                                id = "free_${System.currentTimeMillis()}_${placedBricks.size}",
                                brickType = selectedType,
                                color = selectedColor,
                                gridX = x,
                                gridY = y,
                                rotation = rotation
                            )
                        )
                    },
                    onBrickClicked = { clickedBrick ->
                        soundHapticManager.playPopSound()
                        placedBricks.remove(clickedBrick)
                    }
                )
            }

            // Bottom Tray: Bricks & Rotation & Celebrate
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                color = Color(0xFF1F2937),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                shadowElevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CHOOSE A BRICK (${placedBricks.size} placed)",
                            color = Color(0xFF9CA3AF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Button(
                                onClick = {
                                    soundHapticManager.playPickupSound()
                                    rotation = if (rotation == 0) 90 else 0
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF374151)),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Rotate 🔄", fontSize = 12.sp, color = Color.White)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if (placedBricks.isNotEmpty()) {
                                        soundHapticManager.playCelebrationSound()
                                        speechManager.speak("Look at what you created, Theo! What an incredible master builder masterpiece!")
                                        starlightManager.addStars(2)
                                        showCelebration = true
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700)),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("Admire! ✨", fontSize = 12.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        availableTypes.forEach { type ->
                            val isSelected = type == selectedType
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color(0xFFFFD700) else Color.DarkGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(6.dp)
                                    .clickable {
                                        soundHapticManager.playPickupSound()
                                        selectedType = type
                                    }
                            ) {
                                LegoBrickView(
                                    brickType = type,
                                    color = selectedColor,
                                    studSizeDp = 26.dp,
                                    rotation = rotation,
                                    isSelected = isSelected
                                )
                            }
                        }
                    }
                }
            }
        }

        CelebrationOverlay(
            isVisible = showCelebration,
            title = "Theo's Masterpiece!",
            subtitle = "You placed ${placedBricks.size} bricks into an awesome custom creation!",
            starsEarned = 2,
            buttonText = "Keep Building! 🚀",
            onDismiss = { showCelebration = false }
        )
    }
}
