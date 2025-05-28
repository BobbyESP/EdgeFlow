package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.Container
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import com.bobbyesp.imagingedge.data.remote.model.browse.Item
import com.bobbyesp.imagingedge.data.remote.soap.requests.BrowseDirectoryRequest
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import com.bobbyesp.imagingedge.domain.model.ImageFile
import io.ktor.client.HttpClient

/**
 * Service to browse UPnP ContentDirectory and map results to domain models.
 *
 * @param config   ImagingEdge configuration (host, port, debug).
 * @param httpClient  Preconfigured Ktor HTTP client.
 */
class ContentService(
    config: ImagingEdgeConfig, httpClient: HttpClient
) : CameraService(
    config = config, httpClient = httpClient
) {
    private val servicePath = "upnp/control/ContentDirectory"

    /**
     * Browse a directory by its [objectId], returning a list of [DirectoryEntry].
     *
     * @param objectId   The UPnP container ID to browse.
     * @param startIndex Starting index for pagination (default is 0).
     * @param preferredSize Optional preferred [DownloadSize] for image resolutions.
     * @return List of parsed directory entries (folders and items).
     */
    suspend fun browseDirectory(
        objectId: String, startIndex: Int = 0, preferredSize: DownloadSize? = null
    ): List<DirectoryEntry> {
        // 1) Build and send SOAP request
//        val envelope = soapBodyBuilder.buildSoapBody(
//            Browse(
//                objectId = objectId,
//                browseFlag = "BrowseDirectChildren",
//                filter = "*",
//                startingIndex = startIndex,
//                requestedCount = 100, // Default page size
//            )
//        )
        val soapResponse = callSoap(
            path = servicePath,
            request = BrowseDirectoryRequest(
                objectId = objectId,
                startingIndex = startIndex,
                requestedCount = 100
            )
        )

        val response = xmlParser.decodeFromString(BrowseResponse.serializer(), soapResponse)
        val didl = response.Result

        // 4) Map DTOs to domain entries
        return mutableListOf<DirectoryEntry>().apply {
            didl.container?.forEach { add(it.toDirectoryEntry(isDirectory = true)) }
            didl.item?.filter { item ->
                preferredSize == null || item.res.any { res ->
                    res.protocolInfo?.contains("_${preferredSize.name}") == true
                }
            }?.forEach { add(it.toDirectoryEntry(isDirectory = false)) }
        }
    }

    /**
     * Selects the best [ImageFile] representation from a [DirectoryEntry].
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
        val chosenRes = item.res.let { resolutions ->
            preferredSize?.let { size ->
                resolutions.filter { it.protocolInfo?.contains("_${size.name}") == true }
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
     * Extension to map [Container] or [Item] DTO to [DirectoryEntry].
     */
    private fun Container.toDirectoryEntry(isDirectory: Boolean) =
        DirectoryEntry(id = id, title = title, isDirectory = isDirectory, rawXml = this)

    private fun Item.toDirectoryEntry(isDirectory: Boolean) =
        DirectoryEntry(id = null, title = title, isDirectory = isDirectory, rawXml = this)
}