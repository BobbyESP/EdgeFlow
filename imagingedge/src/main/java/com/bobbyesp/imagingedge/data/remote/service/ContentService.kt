package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.model.Container
import com.bobbyesp.imagingedge.data.remote.model.Envelope
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import com.bobbyesp.imagingedge.data.remote.model.browse.Item
import com.bobbyesp.imagingedge.data.remote.soap.requests.BrowseDirectoryRequest
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import com.bobbyesp.imagingedge.domain.model.ImageFile
import io.ktor.client.HttpClient
import kotlinx.serialization.decodeFromString

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
    override val servicePath = "upnp/control/ContentDirectory"

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
        // Build and send SOAP request
        val soapResponse = callSoap(
            path = servicePath, request = BrowseDirectoryRequest(
                objectId = objectId, startingIndex = startIndex, requestedCount = 100
            )
        )

        val response = xmlParser.decodeFromString<Envelope<BrowseResponse>>(
            soapResponse
        )
        val didl = response.body.data.result

        // Map DTOs to domain entries
        return mutableListOf<DirectoryEntry>().apply {
            didl.container?.forEach { add(it.toDirectoryEntry(isDirectory = true)) }
            didl.item?.filter { item -> shouldIncludeItem(item, preferredSize) }
                ?.forEach { add(it.toDirectoryEntry(isDirectory = false)) }
        }
    }

    private fun shouldIncludeItem(item: Item, preferredSize: DownloadSize?): Boolean {
        if (preferredSize == null) return true
        return item.res.any { res ->
            when (preferredSize) {
                DownloadSize.ORG -> res.url.contains("ORG_")
                else -> res.protocolInfo?.contains("_${preferredSize.name}") == true
            }
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

        val resolutions = item.res

        val chosenRes = if (preferredSize != null) {
            resolutions.filter {
                shouldIncludeItem(
                    item,
                    preferredSize
                )
            } // Renamed 'size' to 'preferredSize' for clarity
                .maxByOrNull { it.size ?: 0 }
        } else {
            resolutions.maxByOrNull { it.size ?: 0 }
        }
            ?: throw IllegalStateException("No resolutions found for ${entry.title}") // Improved error message

        return ImageFile(
            name = entry.title,
            url = chosenRes.url,
            size = chosenRes.size
                ?: -1, // Consider if -1 is the best default or if null is acceptable
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