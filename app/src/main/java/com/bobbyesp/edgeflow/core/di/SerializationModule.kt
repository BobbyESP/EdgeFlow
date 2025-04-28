package com.bobbyesp.edgeflow.core.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val serializationModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }
}