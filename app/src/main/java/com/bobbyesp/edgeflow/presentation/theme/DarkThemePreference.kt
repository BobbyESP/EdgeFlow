package com.bobbyesp.edgeflow.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bobbyesp.edgeflow.R

data class DarkThemePreference(
    val darkThemeValue: Int = FOLLOW_SYSTEM,
    val isHighContrastModeEnabled: Boolean = false
) {

    companion object {
        const val FOLLOW_SYSTEM = 1
        const val ON = 2
        const val OFF = 3
    }

    @Composable
    fun isDarkTheme(): Boolean = when (darkThemeValue) {
        FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ON -> true
        else -> false
    }

    @Composable
    fun getDarkThemeDesc(): String = when (darkThemeValue) {
        FOLLOW_SYSTEM -> stringResource(R.string.follow_system)
        ON -> stringResource(R.string.on)
        OFF -> stringResource(R.string.off)
        else -> stringResource(R.string.follow_system)
    }
}