package com.bobbyesp.edgeflow

import android.app.Application
import com.bobbyesp.edgeflow.core.di.serializationModule
import com.bobbyesp.edgeflow.core.di.utilitiesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(serializationModule, utilitiesModule)
        }
        super.onCreate()
    }
}