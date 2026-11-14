package com.pancoast.brickpuzzler.util

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StarlightManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("brick_puzzler_starlight", Context.MODE_PRIVATE)

    private val _stars = MutableStateFlow(prefs.getInt("stars_count", 12)) // Start with bonus 12 stars!
    val stars: StateFlow<Int> = _stars.asStateFlow()

    fun addStars(amount: Int = 1) {
        val updated = _stars.value + amount
        prefs.edit().putInt("stars_count", updated).apply()
        _stars.value = updated
    }

    fun spendStars(amount: Int): Boolean {
        if (_stars.value >= amount) {
            val updated = _stars.value - amount
            prefs.edit().putInt("stars_count", updated).apply()
            _stars.value = updated
            return true
        }
        return false
    }

    fun isSkinUnlocked(skinId: String): Boolean {
        return prefs.getBoolean("skin_$skinId", skinId == "classic")
    }

    fun unlockSkin(skinId: String) {
        prefs.edit().putBoolean("skin_$skinId", true).apply()
    }
}
