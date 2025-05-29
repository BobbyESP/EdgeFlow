package com.bobbyesp.imagingedge.data.remote.soap

import com.bobbyesp.imagingedge.data.remote.model.Envelope
import com.bobbyesp.imagingedge.data.remote.soap.requests.SoapRequest
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML

/**
 * A builder class for creating SOAP body messages.
 *
 * This class uses an [XML] instance to serialize a [SoapRequest] object into a SOAP envelope string.
 *
 * @property xml The [XML] instance used for serialization.
 */
class SoapBodyBuilder(
    val xml: XML
) {
    /**
     * Builds a SOAP body from the given [SoapRequest] data.
     *
     * @param T The type of the SOAP request data.
     * @param data The SOAP request data to be encoded into the SOAP body.
     * @return The XML string representation of the SOAP envelope containing the body.
     */
    inline fun <reified T : SoapRequest> buildSoapBody(
        data: T
    ): String {
        return xml.encodeToString(
            value = Envelope(
                body = Envelope.Body(
                    data
                )
            )
        )
    }
}