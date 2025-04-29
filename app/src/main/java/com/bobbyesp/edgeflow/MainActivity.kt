package com.bobbyesp.edgeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.bobbyesp.edgeflow.presentation.Navigation
import com.bobbyesp.edgeflow.presentation.common.AppCoreCompositionLocals
import com.bobbyesp.edgeflow.presentation.theme.EdgeFlowTheme
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            KoinContext {
                AppCoreCompositionLocals(context = this) {
                    EdgeFlowTheme {
                        // A surface container using the 'background' color from the theme
                        Box(
                            modifier = Modifier.Companion.fillMaxSize(),
                        ) {
                            Navigation()
                        }
                    }
                }
            }
        }
    }
}