package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.soap.SoapBodyBuilder
import com.bobbyesp.imagingedge.data.remote.soap.requests.SoapRequest
import com.bobbyesp.imagingedge.domain.SoapAction
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
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
    protected val xmlParser = XML { /* configure if needed */ }
    protected val soapBodyBuilder = SoapBodyBuilder(xmlParser)

    /**
     * Sends a SOAP request to the specified [path] using the given [request] and returns the raw response.
     * This is an alternative to the [callSoap] method that takes a [SoapAction] and [bodyContent] as parameters.
     * This method is useful when you want to build the SOAP body manually.
     *
     * @param path The path of the resource to send the request to.
     * @param request The [SoapRequest] to send.
     * @return The raw response from the SOAP request as a string.
     */
    protected suspend fun callSoap(
        path: String,
        request: SoapRequest
    ): String {
        val envelopeXml = soapBodyBuilder.buildSoapBody(request)
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
     * @param serializer The Kotlinx Serialization KSerializer for type [T] (the expected response type).
     * @return An instance of [T] deserialized from the SOAP response body.
     */
    protected suspend inline fun <reified T> postFor(
        path: String,
        request: SoapRequest,
        serializer: KSerializer<T>
    ): T {
        val raw = callSoap(path, request)
        val envelope = xmlParser.decodeFromString(
            SoapEnvelope.serializer(SoapBody.serializer(serializer)),
            raw
        )
        if (config.debug) println("SOAP [${request.action}] →\n${envelope.Body.content}")
        return envelope.Body.content.content
    }
}