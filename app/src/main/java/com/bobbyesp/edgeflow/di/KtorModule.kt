package com.bobbyesp.edgeflow.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.serialization.kotlinx.xml.xml
import kotlinx.serialization.json.Json
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML
import org.koin.dsl.module

val ktorModule = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(ContentNegotiation) {

                json(
                    json = get<Json>()
                )

                xml(
                    format = XML {
                        xmlVersion = XmlVersion.XML10
                        xmlDeclMode = XmlDeclMode.Charset
                    }
                )

            }
        }
    }
}