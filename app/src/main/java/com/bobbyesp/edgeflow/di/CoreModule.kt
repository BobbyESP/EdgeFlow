package com.bobbyesp.edgeflow.di

import com.bobbyesp.edgeflow.presentation.theme.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module {
    single<ThemeManager> {
        ThemeManager(
            context = androidContext()
        )
    }
}