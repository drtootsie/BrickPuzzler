package com.pancoast.brickpuzzler

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pancoast.brickpuzzler.ui.screens.BlueprintPuzzleScreen
import com.pancoast.brickpuzzler.ui.screens.FreeBuildScreen
import com.pancoast.brickpuzzler.ui.screens.HomeScreen
import com.pancoast.brickpuzzler.ui.screens.ScreenState
import com.pancoast.brickpuzzler.ui.screens.StudMathScreen
import com.pancoast.brickpuzzler.util.SoundHapticManager
import com.pancoast.brickpuzzler.util.SpeechManager
import com.pancoast.brickpuzzler.util.StarlightManager

class MainActivity : ComponentActivity() {

    private lateinit var soundHapticManager: SoundHapticManager
    private lateinit var speechManager: SpeechManager
    private lateinit var starlightManager: StarlightManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (intent.getBooleanExtra("PIN_TO_HOME", false)) {
            requestPinToHomeScreen()
        }

        soundHapticManager = SoundHapticManager(this)
        speechManager = SpeechManager(this)
        starlightManager = StarlightManager(this)

        setContent {
            BrickPuzzlerApp(
                soundHapticManager = soundHapticManager,
                speechManager = speechManager,
                starlightManager = starlightManager
            )
        }
    }

    private fun requestPinToHomeScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val shortcutManager = getSystemService(ShortcutManager::class.java)
            if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported) {
                val pinShortcutInfo = ShortcutInfo.Builder(this, "brick_puzzler_launcher_pin")
                    .setIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setShortLabel("Brick Puzzler")
                    .setIntent(Intent(Intent.ACTION_MAIN).setComponent(ComponentName(this, MainActivity::class.java)))
                    .build()
                shortcutManager.requestPinShortcut(pinShortcutInfo, null)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechManager.shutdown()
    }
}

@Composable
fun BrickPuzzlerApp(
    soundHapticManager: SoundHapticManager,
    speechManager: SpeechManager,
    starlightManager: StarlightManager
) {
    var currentScreen by remember { mutableStateOf(ScreenState.HOME) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF0F172A)
    ) {
        when (currentScreen) {
            ScreenState.HOME -> {
                HomeScreen(
                    soundHapticManager = soundHapticManager,
                    speechManager = speechManager,
                    starlightManager = starlightManager,
                    onNavigate = { screen -> currentScreen = screen }
                )
            }
            ScreenState.BLUEPRINTS -> {
                BlueprintPuzzleScreen(
                    soundHapticManager = soundHapticManager,
                    speechManager = speechManager,
                    starlightManager = starlightManager,
                    onBack = { currentScreen = ScreenState.HOME }
                )
            }
            ScreenState.STUD_MATH -> {
                StudMathScreen(
                    soundHapticManager = soundHapticManager,
                    speechManager = speechManager,
                    starlightManager = starlightManager,
                    onBack = { currentScreen = ScreenState.HOME }
                )
            }
            ScreenState.FREE_BUILD -> {
                FreeBuildScreen(
                    soundHapticManager = soundHapticManager,
                    speechManager = speechManager,
                    starlightManager = starlightManager,
                    onBack = { currentScreen = ScreenState.HOME }
                )
            }
        }
    }
}
