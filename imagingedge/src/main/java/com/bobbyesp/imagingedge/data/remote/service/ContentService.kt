package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.Container
import com.bobbyesp.imagingedge.data.remote.model.DIDLLite
import com.bobbyesp.imagingedge.data.remote.model.Item
import com.bobbyesp.imagingedge.data.remote.parser.SoapResponseParser
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import com.bobbyesp.imagingedge.domain.model.ImageFile
import com.bobbyesp.imagingedge.domain.SoapAction
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import nl.adaptivity.xmlutil.serialization.XML

/**
 * Service to browse UPnP ContentDirectory and map results to domain models.
 *
 * @param config   ImagingEdge configuration (host, port, debug).
 * @param httpClient  Preconfigured Ktor HTTP client.
 */
class ContentService(
    private val config: ImagingEdgeConfig,
    private val httpClient: HttpClient
) {
    private val xmlParser = XML { /* default xmlutil settings */ }
    private val serviceUrl = "${config.baseUrl}/upnp/control/ContentDirectory"

    /**
     * Browse a directory by its [objectId], returning a list of [DirectoryEntry].
     *
     * @param objectId   The UPnP container ID to browse.
     * @param startIndex Starting index for pagination (default is 0).
     * @param preferredSize Optional preferred [DownloadSize] for image resolutions.
     * @return List of parsed directory entries (folders and items).
     */
    suspend fun browseDirectory(
        objectId: String,
        startIndex: Int = 0,
        preferredSize: DownloadSize? = null
    ): List<DirectoryEntry> {
        // 1) Build and send SOAP request
        val envelope = buildBrowseEnvelope(objectId, startIndex)
        val soapResponse = httpClient.post(serviceUrl) {
            header("SOAPACTION", "\"${SoapAction.CONTENT_DIRECTORY}\"")
            contentType(ContentType.Text.Xml)
            setBody(envelope)
        }.bodyAsText(Charsets.UTF_8)

        if (config.debug) println("SOAP Response:\n$soapResponse")

        // 2) Extract and unescape inner <Result> XML
        val innerEscaped = SoapResponseParser.extractInnerXml(soapResponse)
        val innerXml = SoapResponseParser.unescapeXml(innerEscaped)

        if (config.debug) println("Inner DIDL XML:\n$innerXml")

        // 3) Decode to our DTO
        val didl = xmlParser.decodeFromString(DIDLLite.serializer(), innerXml)

        // 4) Map DTOs to domain entries
        return mutableListOf<DirectoryEntry>().apply {
            didl.container?.forEach { add(it.toDirectoryEntry(isDirectory = true)) }
            didl.item?.forEach { add(it.toDirectoryEntry(isDirectory = false)) }
        }
    }

    /**
     * Selects the best [ImageFile] representation from a [DirectoryEntry.item].
     *
     * @param entry         The directory entry containing an [Item] DTO.
     * @param preferredSize Preferred [DownloadSize], or null for best available.
     * @throws IllegalArgumentException if entry.rawXml is not an [Item].
     * @throws IllegalStateException if no resolutions found.
     */
    fun parseImageInfo(entry: DirectoryEntry, preferredSize: DownloadSize?): ImageFile {
        val item = entry.rawXml as? Item
            ?: throw IllegalArgumentException("Entry ${entry.id} does not contain an Item DTO")

        // Choose candidate list based on preferredSize, then pick largest
        val chosenRes = item.res
            .let { resolutions ->
                preferredSize?.let { size ->
                    resolutions
                        .filter { it.protocolInfo?.contains("_${size.name}") == true }
                        .maxByOrNull { it.size ?: 0 }
                } ?: resolutions.maxByOrNull { it.size ?: 0 }
            } ?: throw IllegalStateException("No resolutions for ${entry.title}")

        return ImageFile(
            name = entry.title,
            url = chosenRes.url,
            size = chosenRes.size ?: -1,
            resolution = chosenRes.resolution
        )
    }

    /**
     * Builds the SOAP envelope for a Browse action.
     */
    private fun buildBrowseEnvelope(objectId: String, startIndex: Int): String = """
        <?xml version="1.0" encoding="UTF-8"?>
        <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            <s:Body>
                <u:Browse xmlns:u="urn:schemas-upnp-org:service:ContentDirectory:1">
                    <ObjectID>$objectId</ObjectID>
                    <BrowseFlag>BrowseDirectChildren</BrowseFlag>
                    <Filter>*</Filter>
                    <StartingIndex>$startIndex</StartingIndex>
                    <RequestedCount>9999</RequestedCount>
                    <SortCriteria></SortCriteria>
                </u:Browse>
            </s:Body>
        </s:Envelope>
    """.trimIndent()

    /**
     * Extension to map [Container] or [Item] DTO to [DirectoryEntry].
     */
    private fun Container.toDirectoryEntry(isDirectory: Boolean) =
        DirectoryEntry(id = id, title = title, isDirectory = isDirectory, rawXml = this)

    private fun Item.toDirectoryEntry(isDirectory: Boolean) =
        DirectoryEntry(id = null, title = title, isDirectory = isDirectory, rawXml = this)
}