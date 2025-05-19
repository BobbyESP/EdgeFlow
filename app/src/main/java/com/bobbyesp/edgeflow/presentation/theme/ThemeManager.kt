package com.bobbyesp.edgeflow.presentation.theme

import android.content.Context
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialThemeState
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

@Stable
class ThemeManager(
    private val context: Context
) {

    val currentPaletteStyle: PaletteStyle = PaletteStyle.Expressive
    val currentSeedColor: Color = Color(0xFFA11111)
    val currentDarkThemePreference: DarkThemePreference =
        DarkThemePreference() //Default should be some settings saved by the user

    private val userWantsDynamicColor: Boolean = false //TODO: Retrieve from Settings

    val isDynamicColorEnabled: Boolean
        get() = userWantsDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    @Stable
    val dynamicThemeState: DynamicMaterialThemeState
        @Composable get() = rememberDynamicMaterialThemeState(
            style = currentPaletteStyle,
            seedColor = currentSeedColor,
            isDark = currentDarkThemePreference.isDarkTheme(),
            isAmoled = currentDarkThemePreference.isHighContrastModeEnabled
        )

    val dynamicColorScheme: ColorScheme?
        @Composable get() = if (isDynamicColorEnabled) {
            if (currentDarkThemePreference.isDarkTheme()) {
                dynamicDarkColorScheme(context).let {
                    if (currentDarkThemePreference.isHighContrastModeEnabled) it.copy(
                        surface = Color.Black, background = Color.Black
                    ) else it
                }
            } else {
                dynamicLightColorScheme(context)
            }
        } else null
}