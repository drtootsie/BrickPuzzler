package com.pancoast.brickpuzzler.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pancoast.brickpuzzler.model.BrickColor
import com.pancoast.brickpuzzler.model.BrickType
import com.pancoast.brickpuzzler.ui.components.LegoBrickView
import com.pancoast.brickpuzzler.util.SoundHapticManager
import com.pancoast.brickpuzzler.util.SpeechManager
import com.pancoast.brickpuzzler.util.StarlightManager

enum class ScreenState {
    HOME,
    BLUEPRINTS,
    STUD_MATH,
    FREE_BUILD
}

@Composable
fun HomeScreen(
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager,
    onNavigate: (ScreenState) -> Unit
) {
    val stars by starlightManager.stars.collectAsState()

    LaunchedEffect(Unit) {
        speechManager.speak("Welcome back, Master Builder Theo! What would you like to build today?")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF020617)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                LegoBrickView(
                    brickType = BrickType.B2x2,
                    color = BrickColor.RED,
                    studSizeDp = 22.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "BRICK PUZZLER",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Theo's Master Builder Studio",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF38BDF8)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Starlight Star Vault Banner
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF311042),
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star Vault",
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "STARLIGHT STARS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE9D5FF)
                            )
                            Text(
                                text = "$stars Stars Earned!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF581C87)
                    ) {
                        Text(
                            text = "Level: Master ⭐",
                            color = Color(0xFFF3E8FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Game Mode Cards
            ModeCard(
                title = "🧩 Blueprint Puzzles",
                subtitle = "Build Dog Man, Rockets, Race Cars, Dino & Numberblock Four!",
                badgeText = "6 Blueprints",
                gradient = listOf(Color(0xFF2563EB), Color(0xFF1D4ED8)),
                onClick = {
                    soundHapticManager.playPickupSound()
                    onNavigate(ScreenState.BLUEPRINTS)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModeCard(
                title = "🔢 Lego Stud Math",
                subtitle = "Spatial stud counting, addition & 2x multiplication!",
                badgeText = "Brainy Math",
                gradient = listOf(Color(0xFF7C3AED), Color(0xFF6D28D9)),
                onClick = {
                    soundHapticManager.playPickupSound()
                    onNavigate(ScreenState.STUD_MATH)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ModeCard(
                title = "🎨 Free Build Sandbox",
                subtitle = "Unlimited 10×10 baseplate, 8 brick shapes, all colors!",
                badgeText = "Creative Mode",
                gradient = listOf(Color(0xFF059669), Color(0xFF047857)),
                onClick = {
                    soundHapticManager.playPickupSound()
                    onNavigate(ScreenState.FREE_BUILD)
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Friendly Buddy Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF1E293B).copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🐶 🟥 🚀", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Dog Man & Numberblock Four are cheering for you, Theo!",
                        color = Color(0xFFCBD5E1),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ModeCard(
    title: String,
    subtitle: String,
    badgeText: String,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(Brush.horizontalGradient(gradient))
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.85f),
                    lineHeight = 18.sp
                )
            }
        }
    }
}
