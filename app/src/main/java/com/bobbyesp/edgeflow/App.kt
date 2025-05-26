package com.bobbyesp.edgeflow

import android.app.Application
import com.bobbyesp.edgeflow.di.ktorModule
import com.bobbyesp.edgeflow.di.serializationModule
import com.bobbyesp.edgeflow.di.coreModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(coreModule)
            modules(serializationModule)
            modules(ktorModule)
        }
        super.onCreate()
    }
}