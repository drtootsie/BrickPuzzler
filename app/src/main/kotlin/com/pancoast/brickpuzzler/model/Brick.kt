package com.pancoast.brickpuzzler.model

import androidx.compose.ui.graphics.Color

enum class BrickType(
    val studsX: Int,
    val studsY: Int,
    val displayName: String,
    val isSlope: Boolean = false,
    val hasEyes: Boolean = false
) {
    B1x1(1, 1, "1×1 Brick"),
    B1x2(2, 1, "1×2 Brick"),
    B1x3(3, 1, "1×3 Brick"),
    B1x4(4, 1, "1×4 Brick"),
    B2x2(2, 2, "2×2 Brick"),
    B2x3(3, 2, "2×3 Brick"),
    B2x4(4, 2, "2×4 Brick"),
    SLOPE_2x2(2, 2, "2×2 Roof Slope", isSlope = true),
    EYE_1x1(1, 1, "Eye Brick", hasEyes = true);

    val totalStuds: Int get() = studsX * studsY
}

enum class BrickColor(
    val displayName: String,
    val primary: Color,
    val highlight: Color,
    val shadow: Color,
    val studHighlight: Color
) {
    RED(
        "Lego Red",
        Color(0xFFE53935),
        Color(0xFFFF6F60),
        Color(0xFFAB000D),
        Color(0xFFFF8A80)
    ),
    BLUE(
        "Classic Blue",
        Color(0xFF1E88E5),
        Color(0xFF6AB7FF),
        Color(0xFF005CB2),
        Color(0xFF82B1FF)
    ),
    YELLOW(
        "Bright Yellow",
        Color(0xFFFDD835),
        Color(0xFFFFFF72),
        Color(0xFFC6A700),
        Color(0xFFFFFF8D)
    ),
    GREEN(
        "Grass Green",
        Color(0xFF43A047),
        Color(0xFF76D275),
        Color(0xFF00701A),
        Color(0xFFB9F6CA)
    ),
    ORANGE(
        "Neon Orange",
        Color(0xFFFB8C00),
        Color(0xFFFFBD45),
        Color(0xFFC25E00),
        Color(0xFFFFE082)
    ),
    PURPLE(
        "Royal Purple",
        Color(0xFF8E24AA),
        Color(0xFFC158DC),
        Color(0xFF5C007A),
        Color(0xFFEA80FC)
    ),
    WHITE(
        "Polar White",
        Color(0xFFECEFF1),
        Color(0xFFFFFFFF),
        Color(0xFFB0BEC5),
        Color(0xFFFFFFFF)
    ),
    BLACK(
        "Space Black",
        Color(0xFF37474F),
        Color(0xFF62727B),
        Color(0xFF102027),
        Color(0xFF78909C)
    ),
    GOLD(
        "Golden Master",
        Color(0xFFFFD700),
        Color(0xFFFFF176),
        Color(0xFFC6A700),
        Color(0xFFFFF9C4)
    )
}

data class PlacedBrick(
    val id: String,
    val brickType: BrickType,
    val color: BrickColor,
    val gridX: Int,
    val gridY: Int,
    val rotation: Int = 0 // 0 or 90
) {
    val actualWidth: Int get() = if (rotation == 90) brickType.studsY else brickType.studsX
    val actualHeight: Int get() = if (rotation == 90) brickType.studsX else brickType.studsY
}

data class BlueprintSlot(
    val slotId: String,
    val brickType: BrickType,
    val requiredColor: BrickColor,
    val targetGridX: Int,
    val targetGridY: Int,
    val rotation: Int = 0,
    val hintName: String = ""
)

data class Blueprint(
    val id: String,
    val title: String,
    val emoji: String,
    val storyDescription: String,
    val gridCols: Int = 10,
    val gridRows: Int = 10,
    val slots: List<BlueprintSlot>,
    val completionTitle: String,
    val celebrationSpeech: String
)
