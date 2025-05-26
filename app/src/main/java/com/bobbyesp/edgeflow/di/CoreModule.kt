package com.bobbyesp.edgeflow.di

import com.bobbyesp.edgeflow.presentation.common.routing.Route
import com.bobbyesp.edgeflow.presentation.common.routing.TopLevelBackStack
import com.bobbyesp.edgeflow.presentation.theme.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single<TopLevelBackStack<Any>> {
        TopLevelBackStack(Route.Home)
    }

    single<ThemeManager> {
        ThemeManager(
            context = androidContext()
        )
    }
}