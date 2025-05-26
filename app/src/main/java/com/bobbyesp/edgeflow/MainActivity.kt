package com.bobbyesp.edgeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bobbyesp.edgeflow.presentation.Navigation
import com.bobbyesp.edgeflow.presentation.common.AppCoreCompositionLocals
import com.bobbyesp.edgeflow.presentation.common.routing.TopLevelBackStack
import com.bobbyesp.edgeflow.presentation.theme.EdgeFlowTheme
import com.bobbyesp.edgeflow.presentation.theme.ThemeManager
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext
import kotlin.getValue

class MainActivity : ComponentActivity() {

    val themeManager by inject<ThemeManager>()
    val topLevelBackStack by inject<TopLevelBackStack<Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                AppCoreCompositionLocals(
                    context = this, themeManager = themeManager
                ) {
                    EdgeFlowTheme(
                        themeManager = themeManager
                    ) {
                        // A surface container using the 'background' color from the theme
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background),
                        ) {
                            Navigation(
                                topLevelBackStack = topLevelBackStack
                            )
                        }
                    }
                }
            }
        }
    }
}