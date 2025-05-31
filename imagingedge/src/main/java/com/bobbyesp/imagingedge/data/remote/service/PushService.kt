package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferEndRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferStartRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

/**
 * Push service to interact with the camera.
 *
 * This service allows:
 * - Obtaining the Push service descriptor (DmsDescPush.xml).
 * - Sending the X_TransferStart command so that the camera displays "Transferring" and starts the transfer session.
 * - Sending the X_TransferEnd command so that the camera exits transfer mode.
 *
 * @property config Imaging Edge configuration.
 * @property httpClient HTTP client to make requests.
 */
class PushService(
    config: ImagingEdgeConfig, httpClient: HttpClient
) : CameraService(config, httpClient) {

    override val servicePath = "upnp/control/XPushList"

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
        httpClient.get("${config.baseUrl}/DmsDescPush.xml").bodyAsText()

    /**
     * Sends the X_TransferStart command to the camera.
     * This command instructs the camera to display "Transferring" on its screen
     * and initiate a transfer session.
     */
    suspend fun startTransfer() {
        callSoap<TransferStartRequest>(
            path = servicePath,
            request = TransferStartRequest
        )
    }

    /**
     * Ends the transfer session with the camera.
     * This function sends a SOAP request to the camera to signal the end of the transfer.
     * The camera will exit transfer mode upon receiving this command.
     */
    suspend fun endTransfer() {
        callSoap<TransferEndRequest>(
            path = servicePath, request = TransferEndRequest(0)
        )
    }
}