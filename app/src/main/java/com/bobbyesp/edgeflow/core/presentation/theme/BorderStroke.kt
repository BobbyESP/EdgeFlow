package com.bobbyesp.edgeflow.core.presentation.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
class Borders(
    val extraSmall: BorderStroke,
    val small: BorderStroke,
    val medium: BorderStroke,
    val large: BorderStroke,
    val extraLarge: BorderStroke
)

val appBorders: Borders
    @Composable
    get() = appBorders()

@Composable
private fun appBorders(
    color: Color = MaterialTheme.colorScheme.onSurface,
    alpha: Float = 0.62f
): Borders {
    return remember(color, alpha) {
        Borders(
            extraSmall = BorderStroke(0.5.dp, color.copy(alpha = alpha)),
            small = BorderStroke(1.dp, color.copy(alpha = alpha)),
            medium = BorderStroke(1.5.dp, color.copy(alpha = alpha)),
            large = BorderStroke(2.dp, color.copy(alpha = alpha)),
            extraLarge = BorderStroke(3.dp, color.copy(alpha = alpha))
        )
    }
}