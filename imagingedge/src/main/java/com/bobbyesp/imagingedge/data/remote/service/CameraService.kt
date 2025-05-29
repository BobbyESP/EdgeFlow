package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.model.SoapReturnEnvelope
import com.bobbyesp.imagingedge.data.remote.soap.SoapBodyBuilder
import com.bobbyesp.imagingedge.data.remote.soap.requests.SoapRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML

/**
 * Abstract class representing a generic camera service.
 * This class provides common functionalities for interacting with a camera using SOAP requests.
 *
 * @property config The configuration for ImagingEdge.
 * @property httpClient The HTTP client used for making requests.
 */
abstract class CameraService(
    protected val config: ImagingEdgeConfig,
    protected val httpClient: HttpClient
) {
    protected val xmlParser = XML {
        xmlVersion = XmlVersion.XML10
        xmlDeclMode = XmlDeclMode.Charset
    }

    protected val soapBodyBuilder = SoapBodyBuilder(xmlParser)

    /**
     * Sends a SOAP request to the specified [path] using the given [request] and returns the raw response.
     * This method builds the SOAP body using the provided [request] object, which includes the SOAP action
     * and the body content.
     *
     * @param path The path of the resource to send the request to (e.g., "camera", "system").
     * @param request The [SoapRequest] object containing the SOAP action and the body content.
     * @return The raw response from the SOAP request as a string.
     */
    protected suspend inline fun <reified T : SoapRequest> callSoap(
        path: String,
        request: T
    ): String {
        val envelopeXml = soapBodyBuilder.buildSoapBody(request)
        if (config.debug) {
            println("SOAP [${request.action}] →\n$envelopeXml")
        }
        return httpClient.post("${config.baseUrl}/$path") {
            header("SOAPACTION", "\"${request.action.namespace}#${request.action.action}\"")
            contentType(ContentType.Text.Xml)
            setBody(envelopeXml)
        }.bodyAsText(Charsets.UTF_8)
    }

    /**
     * Sends a SOAP request to the specified [path] with the given [request] object,
     * then deserializes the SOAP Body of the response into an object of type [T] using the provided [serializer].
     * This version of `postFor` is intended for requests where the body content is an object that needs to be serialized to XML.
     *
     * @param T The type to deserialize the SOAP Body content into.
     * @param path The specific path for the camera service endpoint (e.g., "camera", "system").
     * @param request The [SoapRequest] object containing the SOAP action and the content to be serialized into the request body.
     * @return An instance of [T] deserialized from the SOAP response body.
     */
    protected suspend inline fun <reified T: SoapRequest> postFor(
        path: String,
        request: T,
    ): T {
        val raw = callSoap(path, request)
        val envelope = xmlParser.decodeFromString<SoapReturnEnvelope<T>>(
            raw
        )
        if (config.debug) println("SOAP [${request.action}] →\n${envelope.body}")
        return envelope.body
    }
}