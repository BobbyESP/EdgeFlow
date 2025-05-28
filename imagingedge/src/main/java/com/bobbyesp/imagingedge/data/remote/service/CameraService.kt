package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.domain.SoapAction
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.KSerializer
import nl.adaptivity.xmlutil.serialization.XML
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.model.SoapBody

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

    protected fun buildSoapEnvelope(action: SoapAction, bodyContent: String? = null): String = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
                    s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:${action.action} xmlns:u="${action.toString(true)}">
                    ${bodyContent.orEmpty()}
                </u:${action.action}>
            </s:Body>
        </s:Envelope>
    """


    /**
     * Sends a SOAP request to the specified [path] using the given [action] and returns the raw response.
     *
     * @param path The path of the resource to send the request to.
     * @param action The SOAP action to execute.
     * @param bodyContent Optional content for the body of the SOAP request.
     * @return The raw response from the SOAP request as a string.
     */
    protected suspend fun callSoap(
        path: String,
        action: SoapAction,
        bodyContent: String? = null
    ): String {
        val envelope = buildSoapEnvelope(action, bodyContent)
        val response = httpClient.post("${config.baseUrl}/$path") {
            header("SOAPACTION", "\"$action\"")
            contentType(ContentType.Text.Xml)
            setBody(envelope)
        }.bodyAsText(Charsets.UTF_8)

        return response
    }

    /**
     * Sends a SOAP request to the specified [path] with the given [action] and optional [bodyContent],
     * then deserializes the SOAP Body of the response into an object of type [T] using the provided [serializer].
     *
     * @param T The type to deserialize the SOAP Body content into.
     * @param path The specific path for the camera service endpoint (e.g., "camera", "system").
     * @param action The SOAP action to be performed.
     * @param bodyContent Optional XML string content to be included within the SOAP action tags in the request body.
     * @param serializer The Kotlinx Serialization KSerializer for type [T].
     * @return An instance of [T] deserialized from the SOAP response body.
     */
    protected suspend inline fun <reified T> postFor(
        path: String,
        action: SoapAction,
        bodyContent: String? = null,
        serializer: KSerializer<T>
    ): T {
        val raw = callSoap(path, action, bodyContent)
        val envelope = xmlParser.decodeFromString(
            SoapEnvelope.serializer( // The <s:Envelope> wrapper
                SoapBody.serializer( // The <s:Body> wrapper
                    serializer // The actual content type (e.g., <u:X_TransferStartResponse> or <u:Browse>)
                )
            ),
            raw
        )
        if (config.debug) println("SOAP [$action] response content:\n${envelope.Body.content}")
        return envelope.Body.content.content
    }
}