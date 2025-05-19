package com.bobbyesp.edgeflow.presentation.theme

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDirection
import com.materialkolor.DynamicMaterialTheme

@Composable
fun EdgeFlowTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = LocalTextStyle.current.copy(
            lineBreak = LineBreak.Paragraph,
            textDirection = TextDirection.Content
        )
    ) {
        if (themeManager.dynamicColorScheme != null) {
            MaterialTheme(
                colorScheme = themeManager.dynamicColorScheme!!,
                shapes = AppShapes,
                //typography = Typography,
                content = content
            )
        } else {
            DynamicMaterialTheme(
                state = themeManager.dynamicThemeState,
                animate = true,
                shapes = AppShapes,
                //typography = Typography,
                content = content
            )
        }
    }
}