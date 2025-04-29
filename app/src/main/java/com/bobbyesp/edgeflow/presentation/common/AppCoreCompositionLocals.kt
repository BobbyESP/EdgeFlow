package com.bobbyesp.edgeflow.presentation.common

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.materialkolor.DynamicMaterialThemeState
import com.materialkolor.PaletteStyle
import com.materialkolor.rememberDynamicMaterialThemeState
import com.skydoves.landscapist.coil.LocalCoilImageLoader
import kotlinx.coroutines.Dispatchers

val LocalDynamicThemeState =
    compositionLocalOf<DynamicMaterialThemeState> { error("No theme state provided") }
val LocalOrientation = compositionLocalOf<Int> { error("No orientation provided") }
val LocalNavController =
    compositionLocalOf<NavHostController> { error("No nav controller provided") }
val LocalSonner = compositionLocalOf<ToasterState> { error("No Sonner instance provided") }

@Composable
fun AppCoreCompositionLocals(
    context: Context = LocalContext.current,
    content: @Composable () -> Unit
) {
    val navController = rememberNavController()

    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.35)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(7 * 1024 * 1024)
                .build()
        }
        .respectCacheHeaders(false)
        .allowHardware(true)
        .crossfade(true)
        .bitmapFactoryMaxParallelism(12)
        .dispatcher(Dispatchers.IO)
        .build()

    val config = LocalConfiguration.current

    val dynamicThemeState = rememberDynamicMaterialThemeState(
        style = PaletteStyle.TonalSpot,
        seedColor = Color(0xFF080808),
        isDark = isSystemInDarkTheme()
    )

    val sonner = rememberToasterState()

    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalDynamicThemeState provides dynamicThemeState,
        LocalOrientation provides config.orientation,
        LocalCoilImageLoader provides imageLoader,
        LocalSonner provides sonner,
        content = content
    )

}