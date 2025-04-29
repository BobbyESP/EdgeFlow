package com.bobbyesp.edgeflow.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDirection
import com.materialkolor.DynamicMaterialTheme
import com.materialkolor.DynamicMaterialThemeState
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState

@Composable
fun EdgeFlowTheme(
    themeState: DynamicMaterialThemeState = rememberDynamicMaterialThemeState(
        style = PaletteStyle.Monochrome,
        seedColor = Color(0xFF080808),
        isDark = isSystemInDarkTheme()
    ),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val canUseDynamicColor = dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val dynamicColorScheme = if (canUseDynamicColor) {
        if (themeState.isDark) {
            dynamicDarkColorScheme(context).let {
                if (themeState.isAmoled) it.copy(
                    surface = Color.Black,
                    background = Color.Black
                ) else it
            }
        } else {
            dynamicLightColorScheme(context)
        }
    } else null

    ProvideTextStyle(
        value = LocalTextStyle.current.copy(
            lineBreak = LineBreak.Paragraph,
            textDirection = TextDirection.Content
        )
    ) {
        if (dynamicColorScheme != null) {
            MaterialTheme(
                colorScheme = dynamicColorScheme,
                shapes = AppShapes,
                //typography = Typography,
                content = content
            )
        } else {
            DynamicMaterialTheme(
                state = themeState,
                animate = true,
                shapes = AppShapes,
                //typography = Typography,
                content = content
            )
        }
    }
}