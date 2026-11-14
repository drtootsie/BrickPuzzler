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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.pancoast.brickpuzzler.model.Blueprint
import com.pancoast.brickpuzzler.model.BlueprintRepository
import com.pancoast.brickpuzzler.model.BlueprintSlot
import com.pancoast.brickpuzzler.model.BrickType
import com.pancoast.brickpuzzler.model.PlacedBrick
import com.pancoast.brickpuzzler.ui.components.CelebrationOverlay
import com.pancoast.brickpuzzler.ui.components.LegoBaseplateView
import com.pancoast.brickpuzzler.ui.components.LegoBrickView
import com.pancoast.brickpuzzler.util.SoundHapticManager
import com.pancoast.brickpuzzler.util.SpeechManager
import com.pancoast.brickpuzzler.util.StarlightManager

@Composable
fun BlueprintPuzzleScreen(
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager,
    onBack: () -> Unit
) {
    val blueprints = BlueprintRepository.allBlueprints
    var currentBlueprintIndex by remember { mutableIntStateOf(0) }
    val currentBlueprint = blueprints[currentBlueprintIndex]

    val placedBricks = remember(currentBlueprint.id) { mutableStateListOf<PlacedBrick>() }
    var selectedTraySlot by remember { mutableStateOf<BlueprintSlot?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    val stars by starlightManager.stars.collectAsState()

    // Speak intro when blueprint opens
    LaunchedEffect(currentBlueprint.id) {
        speechManager.speak("Let's build the ${currentBlueprint.title}! ${currentBlueprint.storyDescription}")
    }

    // Check completion
    LaunchedEffect(placedBricks.size) {
        if (placedBricks.size == currentBlueprint.slots.size && placedBricks.isNotEmpty()) {
            soundHapticManager.playCelebrationSound()
            speechManager.speak(currentBlueprint.celebrationSpeech)
            starlightManager.addStars(2)
            showCelebration = true
        }
    }

    // Remaining slots to place
    val remainingSlots = currentBlueprint.slots.filter { slot ->
        placedBricks.none { it.id == slot.slotId }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF12121A))
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

                // Blueprint Title Badge
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF232338)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = currentBlueprint.emoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentBlueprint.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }

                // Stars display
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF311B92), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stars",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$stars",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }

            // Blueprint Switcher Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                blueprints.forEachIndexed { index, bp ->
                    val isSelected = index == currentBlueprintIndex
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFFFFD700) else Color(0xFF28283E),
                        modifier = Modifier.clickable {
                            if (index != currentBlueprintIndex) {
                                currentBlueprintIndex = index
                                selectedTraySlot = null
                                showCelebration = false
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = bp.emoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = bp.title,
                                color = if (isSelected) Color.Black else Color.White,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Instructions Banner
            Text(
                text = if (selectedTraySlot == null) "👇 Tap a brick below, then snap it into the blueprint!" else "👉 Tap the glowing slot to snap the brick!",
                color = if (selectedTraySlot == null) Color(0xFF90CAF9) else Color(0xFFFFEB3B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Lego Baseplate Canvas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                LegoBaseplateView(
                    cols = currentBlueprint.gridCols,
                    rows = currentBlueprint.gridRows,
                    cellSizeDp = 34.dp,
                    placedBricks = placedBricks,
                    blueprintSlots = currentBlueprint.slots,
                    onCellClicked = { x, y ->
                        val slot = selectedTraySlot
                        if (slot != null) {
                            // Check if tapped near or on target
                            val inTargetX = x >= slot.targetGridX && x < slot.targetGridX + (if (slot.rotation == 90) slot.brickType.studsY else slot.brickType.studsX)
                            val inTargetY = y >= slot.targetGridY && y < slot.targetGridY + (if (slot.rotation == 90) slot.brickType.studsX else slot.brickType.studsY)

                            if (inTargetX && inTargetY) {
                                // Correct Snap!
                                soundHapticManager.playSnapSound()
                                placedBricks.add(
                                    PlacedBrick(
                                        id = slot.slotId,
                                        brickType = slot.brickType,
                                        color = slot.requiredColor,
                                        gridX = slot.targetGridX,
                                        gridY = slot.targetGridY,
                                        rotation = slot.rotation
                                    )
                                )
                                selectedTraySlot = null
                            } else {
                                // Gently hint
                                soundHapticManager.playPopSound()
                            }
                        } else {
                            // If user tapped directly on an empty slot, find matching slot and auto-snap if brick exists in tray
                            val targetSlot = currentBlueprint.slots.find { s ->
                                x >= s.targetGridX && x < s.targetGridX + s.brickType.studsX &&
                                y >= s.targetGridY && y < s.targetGridY + s.brickType.studsY &&
                                placedBricks.none { pb -> pb.id == s.slotId }
                            }
                            if (targetSlot != null) {
                                soundHapticManager.playSnapSound()
                                placedBricks.add(
                                    PlacedBrick(
                                        id = targetSlot.slotId,
                                        brickType = targetSlot.brickType,
                                        color = targetSlot.requiredColor,
                                        gridX = targetSlot.targetGridX,
                                        gridY = targetSlot.targetGridY,
                                        rotation = targetSlot.rotation
                                    )
                                )
                            }
                        }
                    },
                    onBrickClicked = { clickedBrick ->
                        // Remove brick if Theo wants to adjust
                        soundHapticManager.playPopSound()
                        placedBricks.removeAll { it.id == clickedBrick.id }
                    }
                )
            }

            // Bottom Tray: Bricks to place
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                color = Color(0xFF1E1E2E),
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
                            text = "BRICK TRAY (${placedBricks.size}/${currentBlueprint.slots.size})",
                            color = Color(0xFFB0BEC5),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        IconButton(
                            onClick = {
                                placedBricks.clear()
                                selectedTraySlot = null
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Restart",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        remainingSlots.forEach { slot ->
                            val isSelected = selectedTraySlot?.slotId == slot.slotId
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
                                        selectedTraySlot = if (isSelected) null else slot
                                    }
                            ) {
                                LegoBrickView(
                                    brickType = slot.brickType,
                                    color = slot.requiredColor,
                                    studSizeDp = 28.dp,
                                    isSelected = isSelected
                                )
                            }
                        }

                        if (remainingSlots.isEmpty()) {
                            Text(
                                text = "🎉 All bricks placed! Tap Next Puzzle!",
                                color = Color(0xFF69F0AE),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // Celebration Dialog
        CelebrationOverlay(
            isVisible = showCelebration,
            title = currentBlueprint.completionTitle,
            subtitle = "You completed the ${currentBlueprint.title} blueprint!",
            starsEarned = 2,
            buttonText = if (currentBlueprintIndex < blueprints.size - 1) "Next Blueprint 🚀" else "Play Again! 🌟",
            onDismiss = {
                showCelebration = false
                if (currentBlueprintIndex < blueprints.size - 1) {
                    currentBlueprintIndex++
                } else {
                    currentBlueprintIndex = 0
                }
                placedBricks.clear()
                selectedTraySlot = null
            }
        )
    }
}
