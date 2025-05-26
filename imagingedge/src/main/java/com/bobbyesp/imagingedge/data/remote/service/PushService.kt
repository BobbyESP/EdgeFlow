package com.bobbyesp.imagingedge.data.remote.service

import android.util.Log
import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.domain.SoapAction
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Push service to interact with the camera.
 *
 * This service allows:
 * - Obtaining the Push service descriptor (DmsDescPush.xml).
 * - Sending the X_TransferStart command so that the camera displays "Transferring" and starts the transfer session.
 * - Sending the X_TransferEnd command so that the camera exits transfer mode.
 * - Building the standard SOAP body for XPushList.
 *
 * @property config Imaging Edge configuration.
 * @property httpClient HTTP client to make requests.
 */
class PushService(
    private val config: ImagingEdgeConfig,
    private val httpClient: HttpClient
) {
    /**
     * Retrieves the Push Service Descriptor (DmsDescPush.xml) from the camera.
     *
     * This XML file contains information about the capabilities and endpoints of the Push service
     * provided by the camera.
     *
     * @return A [String] containing the XML content of the Push Service Descriptor.
     * @throws io.ktor.client.plugins.HttpRequestTimeoutException if the request times out.
     * @throws io.ktor.util.network.UnresolvedAddressException if the IP address of the host is unresolved.
     * @throws java.net.ConnectException if the connection is refused by the server.
     * @throws io.ktor.client.plugins.ClientRequestException if the server responds with a non-2xx status code.
     */
    suspend fun getServiceDescription(): String =
        httpClient
            .get("${config.baseUrl}/DmsDescPush.xml")
            .bodyAsText()

    /**
     * Sends the X_TransferStart command to the camera.
     * This command instructs the camera to display "Transferring" on its screen
     * and initiate a transfer session.
     */
    suspend fun startTransfer() {
        val action = SoapAction.TRANSFER_START
        val envelope = buildSoapEnvelope(action, bodyContent = null)
        httpClient.post("${config.baseUrl}/upnp/control/XPushList") {
            header("SOAPACTION", "\"$action\"")
            contentType(ContentType.Text.Xml)
            setBody(envelope)
        }
        Log.d(
            this@PushService::class.simpleName,
            "Transfer started with action: $action"
        )
    }

    /**
     * Ends the transfer session with the camera.
     * This function sends a SOAP request to the camera to signal the end of the transfer.
     * The camera will exit transfer mode upon receiving this command.
     */
    suspend fun endTransfer() {
        val action = SoapAction.TRANSFER_END
        // ErrCode=0 means success (finished transfer with no errors)
        val body = "<ErrCode>0</ErrCode>"
        val envelope = buildSoapEnvelope(action, bodyContent = body)
        httpClient.post("${config.baseUrl}/upnp/control/XPushList") {
            header("SOAPACTION", action)
            contentType(ContentType.Text.Xml)
            setBody(envelope)
        }
        Log.d(
            this@PushService::class.simpleName,
            "Transfer ended with action: $action"
        )
    }

    /**
     * Builds the standard SOAP body for XPushList.
     *
     * @param soapAction The SOAP action to be performed.
     * @param bodyContent The content of the SOAP body.
     * @return The SOAP envelope as a string.
     */
    private fun buildSoapEnvelope(soapAction: SoapAction, bodyContent: String?): String = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/"
                    s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:${soapAction.action} xmlns:u="${soapAction.toString(true)}">
                    ${bodyContent.orEmpty()}
                </u:${soapAction.action}>
            </s:Body>
        </s:Envelope>
    """.trimIndent()
}