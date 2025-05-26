package com.bobbyesp.edgeflow.presentation.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.bobbyesp.edgeflow.presentation.theme.ThemeManager
import com.bobbyesp.edgeflow.util.ext.MB
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.materialkolor.DynamicMaterialThemeState
import com.skydoves.landscapist.coil.LocalCoilImageLoader
import kotlinx.coroutines.Dispatchers

val LocalDynamicThemeState =
    compositionLocalOf<DynamicMaterialThemeState> { error("No theme state provided") }
val LocalOrientation = compositionLocalOf<Int> { error("No orientation provided") }
val LocalSonner = compositionLocalOf<ToasterState> { error("No Sonner instance provided") }

@Composable
fun AppCoreCompositionLocals(
    context: Context = LocalContext.current,
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {

    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.35)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizeBytes(32.MB)
                .build()
        }
        .respectCacheHeaders(false)
        .allowHardware(true)
        .crossfade(true)
        .bitmapFactoryMaxParallelism(6)
        .dispatcher(Dispatchers.IO)
        .build()

    val config = LocalConfiguration.current

    val dynamicThemeState = themeManager.dynamicThemeState

    val sonner = rememberToasterState()

    CompositionLocalProvider(
        LocalDynamicThemeState provides dynamicThemeState,
        LocalOrientation provides config.orientation,
        LocalCoilImageLoader provides imageLoader,
        LocalSonner provides sonner,
        content = content
    )

}