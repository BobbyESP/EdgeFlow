package com.bobbyesp.edgeflow.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.bobbyesp.edgeflow.presentation.common.routing.Route
import com.bobbyesp.edgeflow.presentation.common.routing.TOP_LEVEL_ROUTES
import com.bobbyesp.edgeflow.presentation.common.routing.TopLevelBackStack
import com.bobbyesp.edgeflow.presentation.screens.home.MainScreen

@Composable
fun Navigation(
    topLevelBackStack: TopLevelBackStack<Any>
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                TOP_LEVEL_ROUTES.forEach { topLevelRoute ->

                    val isSelected = topLevelRoute == topLevelBackStack.topLevelKey
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            topLevelBackStack.addTopLevel(topLevelRoute)
                        },
                        icon = {
                            Icon(
                                imageVector = topLevelRoute.icon,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            backStack = topLevelBackStack.backStack,
            entryProvider = entryProvider {
                entry<Route.Home> {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            MainScreen()
                        }
                    }
                }

                entry<Route.Settings> {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Settings Screen")
                        }
                    }
                }

                entry<Route.Profile> {
                    Scaffold { paddingValues ->
                        Box(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Profile Screen")
                        }
                    }
                }
            }
        )
    }
}