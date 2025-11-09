package br.com.petguard.ui.theme

import androidx.compose.runtime.mutableStateOf

object ThemeManager {
    val isDarkTheme = mutableStateOf(false)

    fun toggleTheme(enabled: Boolean) {
        isDarkTheme.value = enabled
    }
}