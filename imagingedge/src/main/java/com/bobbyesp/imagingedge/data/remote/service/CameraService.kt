package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.Envelope
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import com.bobbyesp.imagingedge.data.remote.model.error.SoapFault
import com.bobbyesp.imagingedge.data.remote.soap.SoapBodyBuilder
import com.bobbyesp.imagingedge.data.remote.soap.requests.BrowseDirectoryRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.SoapRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferEndRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferStartRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
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
    protected val logger = KotlinLogging.logger(this::class.simpleName ?: "CameraService")

    abstract val servicePath: String

    private val module = SerializersModule {
        // The base class has to be Any because XML Util is not able to handle polymorphic serialization
        // with a sealed class / interface hierarchy, so we use Any as the base type.
        polymorphic(Any::class) {
            // Responses
            subclass(BrowseResponse::class, BrowseResponse.serializer())
            subclass(SoapFault::class, SoapFault.serializer())

            //Requests
            subclass(TransferStartRequest::class, TransferStartRequest.serializer())
            subclass(TransferEndRequest::class, TransferEndRequest.serializer())
            subclass(BrowseDirectoryRequest::class, BrowseDirectoryRequest.serializer())
        }
    }

    val xmlParser = XML(module) {
        xmlVersion = XmlVersion.XML10
        xmlDeclMode = XmlDeclMode.Charset
        autoPolymorphic = true
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
        return httpClient.post("${config.baseUrl}/$path") {
            header("SOAPACTION", "\"${request.action.namespace}#${request.action.action}\"")
            contentType(ContentType.Text.Xml)
            setBody(envelopeXml)
        }.bodyAsText(Charsets.UTF_8)
    }

    /**
     * Sends a SOAP request to the specified [path] with the given [request] object,
     * then deserializes the SOAP Body of the response into an object of type [T].
     * This method is a convenience wrapper around [callSoap] that handles the
     * deserialization of the response envelope and extracts the data from the body.
     *
     * @param T The type of the [SoapRequest] and the expected type of the data within the SOAP response body.
     * @param path The specific path for the camera service endpoint (e.g., "camera", "system").
     * @param request The [SoapRequest] object containing the SOAP action and any necessary request parameters.
     * @return An instance of [T] deserialized from the SOAP response body's data field.
     */
    protected suspend inline fun <reified T : SoapRequest> postFor(
        path: String,
        request: T,
    ): T {
        val raw = callSoap(path, request)
        val envelope = xmlParser.decodeFromString<Envelope<T>>(
            raw
        )
        return envelope.body.data
    }
}