package com.pancoast.brickpuzzler.model

object BlueprintRepository {

    val allBlueprints = listOf(
        Blueprint(
            id = "rocket",
            title = "Space Rocket",
            emoji = "🚀",
            storyDescription = "Build a super-fast spaceship to explore the stars with Supa Buddies!",
            gridCols = 8,
            gridRows = 9,
            slots = listOf(
                // Nose cone slope
                BlueprintSlot("r_tip", BrickType.SLOPE_2x2, BrickColor.RED, targetGridX = 3, targetGridY = 0, hintName = "Nose Cone"),
                // Rocket cabin
                BlueprintSlot("r_cab1", BrickType.B2x2, BrickColor.WHITE, targetGridX = 3, targetGridY = 2, hintName = "Cockpit Upper"),
                BlueprintSlot("r_win", BrickType.B2x2, BrickColor.BLUE, targetGridX = 3, targetGridY = 4, hintName = "Space Window"),
                BlueprintSlot("r_cab2", BrickType.B2x2, BrickColor.WHITE, targetGridX = 3, targetGridY = 6, hintName = "Engine Base"),
                // Booster fins
                BlueprintSlot("r_fin_l", BrickType.B1x3, BrickColor.RED, targetGridX = 2, targetGridY = 5, hintName = "Left Fin"),
                BlueprintSlot("r_fin_r", BrickType.B1x3, BrickColor.RED, targetGridX = 5, targetGridY = 5, hintName = "Right Fin"),
                // Fiery thrusters
                BlueprintSlot("r_fire", BrickType.B2x2, BrickColor.ORANGE, targetGridX = 3, targetGridY = 8, hintName = "Blast Fire")
            ),
            completionTitle = "Blast Off!",
            celebrationSpeech = "3, 2, 1, Blast off! Amazing rocket build, Theo! Starlight stars acquired!"
        ),
        Blueprint(
            id = "numberblock_four",
            title = "Numberblock Four",
            emoji = "🟥",
            storyDescription = "Numberblock Four loves being a square! Help him build his super sturdy square body!",
            gridCols = 8,
            gridRows = 8,
            slots = listOf(
                BlueprintSlot("nb4_tl", BrickType.B1x1, BrickColor.RED, targetGridX = 3, targetGridY = 3, hintName = "Top-Left"),
                BlueprintSlot("nb4_tr", BrickType.EYE_1x1, BrickColor.RED, targetGridX = 4, targetGridY = 3, hintName = "Friendly Eye"),
                BlueprintSlot("nb4_bl", BrickType.B1x1, BrickColor.RED, targetGridX = 3, targetGridY = 4, hintName = "Bottom-Left"),
                BlueprintSlot("nb4_br", BrickType.B1x1, BrickColor.RED, targetGridX = 4, targetGridY = 4, hintName = "Bottom-Right")
            ),
            completionTitle = "I am Four, and I am a Square!",
            celebrationSpeech = "One, two, three, four! Four square blocks! Splendid job, Theo!"
        ),
        Blueprint(
            id = "dog_man",
            title = "Dog Man's Badge",
            emoji = "🐶",
            storyDescription = "The heroic cop Dog Man needs his badge and police cap assembled!",
            gridCols = 8,
            gridRows = 9,
            slots = listOf(
                // Police Cap
                BlueprintSlot("dm_cap", BrickType.B2x4, BrickColor.BLUE, targetGridX = 2, targetGridY = 1, hintName = "Police Cap"),
                // Head
                BlueprintSlot("dm_head_l", BrickType.B2x2, BrickColor.ORANGE, targetGridX = 2, targetGridY = 3, hintName = "Dog Face Left"),
                BlueprintSlot("dm_eye", BrickType.EYE_1x1, BrickColor.WHITE, targetGridX = 4, targetGridY = 3, hintName = "Dog Man Eye"),
                BlueprintSlot("dm_snout", BrickType.B1x2, BrickColor.BLACK, targetGridX = 4, targetGridY = 4, hintName = "Sniffing Snout"),
                // Officer Uniform / Gold Badge
                BlueprintSlot("dm_badge", BrickType.B2x2, BrickColor.GOLD, targetGridX = 3, targetGridY = 5, hintName = "Chief Gold Badge"),
                BlueprintSlot("dm_body", BrickType.B2x4, BrickColor.BLUE, targetGridX = 2, targetGridY = 7, hintName = "Police Uniform")
            ),
            completionTitle = "Dog Man to the Rescue!",
            celebrationSpeech = "Woof! Dog Man gives you two paws up! Supa Buddies celebrate with you, Theo!"
        ),
        Blueprint(
            id = "race_car",
            title = "Lightning Racer",
            emoji = "🏎️",
            storyDescription = "Assemble a turbo racing car that can zoom around the track at top speed!",
            gridCols = 10,
            gridRows = 8,
            slots = listOf(
                // Spoiler
                BlueprintSlot("rc_spoil", BrickType.B1x4, BrickColor.YELLOW, targetGridX = 1, targetGridY = 2, hintName = "Rear Wing"),
                // Body chassis
                BlueprintSlot("rc_chassis", BrickType.B2x4, BrickColor.RED, targetGridX = 3, targetGridY = 3, hintName = "Turbo Chassis"),
                BlueprintSlot("rc_nose", BrickType.SLOPE_2x2, BrickColor.RED, targetGridX = 7, targetGridY = 3, hintName = "Aero Nose"),
                // Cockpit
                BlueprintSlot("rc_glass", BrickType.B1x2, BrickColor.BLUE, targetGridX = 4, targetGridY = 2, hintName = "Windshield"),
                // Wheels
                BlueprintSlot("rc_wh_rl", BrickType.B1x2, BrickColor.BLACK, targetGridX = 2, targetGridY = 5, hintName = "Back Wheel"),
                BlueprintSlot("rc_wh_fr", BrickType.B1x2, BrickColor.BLACK, targetGridX = 6, targetGridY = 5, hintName = "Front Wheel")
            ),
            completionTitle = "Vroom Vroom!",
            celebrationSpeech = "Full throttle! The Lightning Racer crosses the finish line in first place!"
        ),
        Blueprint(
            id = "dino_rex",
            title = "T-Rex Dino",
            emoji = "🦖",
            storyDescription = "Build a mighty green dinosaur with roaring jaws and a swishing tail!",
            gridCols = 10,
            gridRows = 9,
            slots = listOf(
                // Head and Jaws
                BlueprintSlot("d_head", BrickType.B2x3, BrickColor.GREEN, targetGridX = 5, targetGridY = 1, hintName = "T-Rex Head"),
                BlueprintSlot("d_eye", BrickType.EYE_1x1, BrickColor.WHITE, targetGridX = 6, targetGridY = 1, hintName = "Dino Eye"),
                BlueprintSlot("d_jaw", BrickType.B1x2, BrickColor.GREEN, targetGridX = 7, targetGridY = 2, hintName = "Teeth Jaw"),
                // Body
                BlueprintSlot("d_body", BrickType.B2x4, BrickColor.GREEN, targetGridX = 3, targetGridY = 3, hintName = "Mighty Body"),
                // Tail
                BlueprintSlot("d_tail", BrickType.B1x3, BrickColor.GREEN, targetGridX = 0, targetGridY = 4, hintName = "Swishing Tail"),
                // Legs
                BlueprintSlot("d_leg_l", BrickType.B2x2, BrickColor.GREEN, targetGridX = 3, targetGridY = 5, hintName = "Left Foot"),
                BlueprintSlot("d_leg_r", BrickType.B2x2, BrickColor.GREEN, targetGridX = 5, targetGridY = 5, hintName = "Right Foot")
            ),
            completionTitle = "ROOOOAR!",
            celebrationSpeech = "Rooaaar! The Mighty T-Rex is alive! High five, master builder Theo!"
        ),
        Blueprint(
            id = "castle",
            title = "Knight's Fortress",
            emoji = "🏰",
            storyDescription = "A castle with sturdy stone towers, battlements, and a golden treasure room!",
            gridCols = 8,
            gridRows = 8,
            slots = listOf(
                // Left tower
                BlueprintSlot("c_tl", BrickType.B2x2, BrickColor.BLACK, targetGridX = 1, targetGridY = 2, hintName = "Left Tower"),
                BlueprintSlot("c_bat_l", BrickType.B1x1, BrickColor.RED, targetGridX = 1, targetGridY = 1, hintName = "Left Flag"),
                // Right tower
                BlueprintSlot("c_tr", BrickType.B2x2, BrickColor.BLACK, targetGridX = 5, targetGridY = 2, hintName = "Right Tower"),
                BlueprintSlot("c_bat_r", BrickType.B1x1, BrickColor.RED, targetGridX = 6, targetGridY = 1, hintName = "Right Flag"),
                // Center Gate & Vault
                BlueprintSlot("c_gate", BrickType.B2x4, BrickColor.BLACK, targetGridX = 2, targetGridY = 4, hintName = "Castle Gate"),
                BlueprintSlot("c_gold", BrickType.B2x2, BrickColor.GOLD, targetGridX = 3, targetGridY = 6, hintName = "Treasure Vault")
            ),
            completionTitle = "Kingdom Secured!",
            celebrationSpeech = "Hear ye, hear ye! Sir Theo has built an impenetrable castle fortress!"
        )
    )
}
