package com.bobbyesp.imagingedge.data.remote.soap

import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.soap.requests.SoapRequest
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML

class SoapBodyBuilder(
    val xml: XML
) {
    inline fun <reified T : SoapRequest> buildSoapBody(
        data: T
    ): String {
        return xml.encodeToString(
            value = SoapEnvelope(
                body = SoapBody(
                    data
                )
            )
        )
    }
}