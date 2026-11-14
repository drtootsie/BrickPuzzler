package com.pancoast.brickpuzzler.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pancoast.brickpuzzler.model.BrickColor
import com.pancoast.brickpuzzler.model.BrickType
import com.pancoast.brickpuzzler.ui.components.CelebrationOverlay
import com.pancoast.brickpuzzler.ui.components.LegoBrickView
import com.pancoast.brickpuzzler.util.SoundHapticManager
import com.pancoast.brickpuzzler.util.SpeechManager
import com.pancoast.brickpuzzler.util.StarlightManager

data class StudQuestion(
    val prompt: String,
    val spokenPrompt: String,
    val brickA: BrickType,
    val colorA: BrickColor,
    val brickB: BrickType? = null,
    val colorB: BrickColor? = null,
    val isAddition: Boolean = false,
    val options: List<Int>,
    val correctIndex: Int,
    val explanation: String
)

@Composable
fun StudMathScreen(
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager,
    onBack: () -> Unit
) {
    val questions = remember {
        listOf(
            StudQuestion(
                prompt = "How many studs does this 2×2 brick have?",
                spokenPrompt = "How many studs does this two by two red brick have, Theo?",
                brickA = BrickType.B2x2,
                colorA = BrickColor.RED,
                options = listOf(2, 3, 4, 6),
                correctIndex = 2,
                explanation = "2 rows of 2 studs = 4 studs! A perfect square!"
            ),
            StudQuestion(
                prompt = "Add them up! 🟦 (2 studs) + 🟩 (4 studs) = ?",
                spokenPrompt = "Add them up! Two studs plus four studs equals how many?",
                brickA = BrickType.B1x2,
                colorA = BrickColor.BLUE,
                brickB = BrickType.B1x4,
                colorB = BrickColor.GREEN,
                isAddition = true,
                options = listOf(5, 6, 7, 8),
                correctIndex = 1,
                explanation = "2 + 4 = 6 studs!"
            ),
            StudQuestion(
                prompt = "Count the studs on this 2×3 brick! (2 × 3 = ?)",
                spokenPrompt = "Count the studs on this two by three yellow brick. Two times three is?",
                brickA = BrickType.B2x3,
                colorA = BrickColor.YELLOW,
                options = listOf(5, 6, 8, 9),
                correctIndex = 1,
                explanation = "2 times 3 equals 6 studs!"
            ),
            StudQuestion(
                prompt = "Super Studs! How many studs on a big 2×4 brick?",
                spokenPrompt = "Super studs! How many studs on a big two by four orange brick?",
                brickA = BrickType.B2x4,
                colorA = BrickColor.ORANGE,
                options = listOf(6, 7, 8, 10),
                correctIndex = 2,
                explanation = "2 times 4 equals 8 studs! Fantastic spatial math!"
            ),
            StudQuestion(
                prompt = "Stacking Power! 🟥 (4 studs) + 🟨 (4 studs) = ?",
                spokenPrompt = "Stacking power! Four studs plus four studs equals how many total?",
                brickA = BrickType.B2x2,
                colorA = BrickColor.RED,
                brickB = BrickType.B2x2,
                colorB = BrickColor.YELLOW,
                isAddition = true,
                options = listOf(6, 8, 9, 10),
                correctIndex = 1,
                explanation = "4 + 4 = 8 studs! Just like Numberblock Eight!"
            ),
            StudQuestion(
                prompt = "Multiplication Magic: 1×4 brick + 1×4 brick = ?",
                spokenPrompt = "One times four plus one times four. How many studs altogether?",
                brickA = BrickType.B1x4,
                colorA = BrickColor.BLUE,
                brickB = BrickType.B1x4,
                colorB = BrickColor.PURPLE,
                isAddition = true,
                options = listOf(4, 7, 8, 12),
                correctIndex = 2,
                explanation = "4 + 4 = 8 studs! You're a math genius Theo!"
            )
        )
    }

    var questionIndex by remember { mutableIntStateOf(0) }
    val currentQ = questions[questionIndex % questions.size]
    var selectedOption by remember(questionIndex) { mutableStateOf<Int?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    val stars by starlightManager.stars.collectAsState()

    LaunchedEffect(questionIndex) {
        speechManager.speak(currentQ.spokenPrompt)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                    color = Color(0xFF1E293B)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🔢", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Lego Stud Math",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "QUESTION ${questionIndex + 1} OF ${questions.size}",
                        color = Color(0xFF38BDF8),
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentQ.prompt,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Visual Bricks Display
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF1E3A8A).copy(alpha = 0.3f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentQ.isAddition && currentQ.brickB != null && currentQ.colorB != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            LegoBrickView(
                                brickType = currentQ.brickA,
                                color = currentQ.colorA,
                                studSizeDp = 44.dp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = "+",
                                color = Color(0xFFFFD700),
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            LegoBrickView(
                                brickType = currentQ.brickB,
                                color = currentQ.colorB,
                                studSizeDp = 44.dp
                            )
                        }
                    } else {
                        LegoBrickView(
                            brickType = currentQ.brickA,
                            color = currentQ.colorA,
                            studSizeDp = 52.dp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Answer Options Grid (4 large buttons)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton(
                        modifier = Modifier.weight(1f),
                        value = currentQ.options[0],
                        isCorrect = currentQ.correctIndex == 0,
                        isSelected = selectedOption == 0
                    ) {
                        handleAnswer(0, currentQ, soundHapticManager, speechManager, starlightManager) {
                            selectedOption = 0
                            showCelebration = true
                        }
                    }
                    OptionButton(
                        modifier = Modifier.weight(1f),
                        value = currentQ.options[1],
                        isCorrect = currentQ.correctIndex == 1,
                        isSelected = selectedOption == 1
                    ) {
                        handleAnswer(1, currentQ, soundHapticManager, speechManager, starlightManager) {
                            selectedOption = 1
                            showCelebration = true
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OptionButton(
                        modifier = Modifier.weight(1f),
                        value = currentQ.options[2],
                        isCorrect = currentQ.correctIndex == 2,
                        isSelected = selectedOption == 2
                    ) {
                        handleAnswer(2, currentQ, soundHapticManager, speechManager, starlightManager) {
                            selectedOption = 2
                            showCelebration = true
                        }
                    }
                    OptionButton(
                        modifier = Modifier.weight(1f),
                        value = currentQ.options[3],
                        isCorrect = currentQ.correctIndex == 3,
                        isSelected = selectedOption == 3
                    ) {
                        handleAnswer(3, currentQ, soundHapticManager, speechManager, starlightManager) {
                            selectedOption = 3
                            showCelebration = true
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        CelebrationOverlay(
            isVisible = showCelebration,
            title = "CORRECT! 🎯",
            subtitle = currentQ.explanation,
            starsEarned = 1,
            buttonText = "Next Math Question! 🌟",
            onDismiss = {
                showCelebration = false
                selectedOption = null
                questionIndex++
            }
        )
    }
}

private fun handleAnswer(
    index: Int,
    question: StudQuestion,
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager,
    onSuccess: () -> Unit
) {
    if (index == question.correctIndex) {
        soundHapticManager.playCelebrationSound()
        starlightManager.addStars(1)
        speechManager.speak("That's right Theo! ${question.explanation}")
        onSuccess()
    } else {
        soundHapticManager.playPopSound()
        speechManager.speak("Almost! Count each round stud on top, Theo!")
    }
}

@Composable
private fun OptionButton(
    modifier: Modifier = Modifier,
    value: Int,
    isCorrect: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isSelected && isCorrect -> Color(0xFF00E676)
                isSelected && !isCorrect -> Color(0xFFFF5252)
                else -> Color(0xFF334155)
            }
        )
    ) {
        Text(
            text = "$value",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}
