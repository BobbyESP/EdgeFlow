package com.bobbyesp.edgeflow.presentation.common.routing

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Propane
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface Route {

    data object Home : Route, TopLevelRoute {
        override val icon: ImageVector
            get() = Icons.Rounded.Home
    }

    data object Settings : Route, TopLevelRoute {
        override val icon: ImageVector
            get() = Icons.Rounded.Settings // Replace with actual settings icon
    }

    data object Profile : Route, TopLevelRoute {
        override val icon: ImageVector
            get() = Icons.Rounded.Propane // Replace with actual profile icon
    }

}

interface TopLevelRoute {
    val icon: ImageVector
}

val TOP_LEVEL_ROUTES = listOf<TopLevelRoute>(
    Route.Home,
    Route.Settings,
    Route.Profile
)