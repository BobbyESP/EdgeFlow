package com.bobbyesp.imagingedge.data.remote.soap

import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML

class SoapBodyBuilder(
    val xml: XML
) {
    inline fun <reified T : Any> buildSoapBody(
        data: T
    ): String {
        return xml.encodeToString(
            value = SoapEnvelope(
                Body = SoapBody(
                    data
                )
            )
        ).let {
            """<?xml version="1.0" encoding=UTF-8"?> $it"""
        }
    }
}