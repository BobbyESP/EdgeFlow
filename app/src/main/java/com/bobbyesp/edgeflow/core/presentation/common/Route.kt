package com.bobbyesp.edgeflow.core.presentation.common

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object App : Route {
        @Serializable
        data object HomeNavigator : Route {
            @Serializable
            data object Home : Route
        }
    }
}