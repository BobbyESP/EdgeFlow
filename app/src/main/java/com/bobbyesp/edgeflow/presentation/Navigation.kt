package com.bobbyesp.edgeflow.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.bobbyesp.foundation.ui.motion.animatedComposable
import com.bobbyesp.edgeflow.presentation.common.LocalNavController
import com.bobbyesp.edgeflow.presentation.common.Route

@Composable
fun Navigation(
    navController: NavHostController = LocalNavController.current
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize(),
        navController = navController,
        route = Route.App::class,
        startDestination = Route.App.HomeNavigator,
    ) {
        navigation<Route.App.HomeNavigator>(
            startDestination = Route.App.HomeNavigator.Home
        ) {
            animatedComposable<Route.App.HomeNavigator.Home> {
                Text("Home")
            }
        }
    }
}