package com.bobbyesp.imagingedge.data.remote

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.downloader.FileDownloader
import com.bobbyesp.imagingedge.data.remote.service.ContentService
import com.bobbyesp.imagingedge.data.remote.service.PushService
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import com.bobbyesp.imagingedge.domain.model.ImageFile
import com.bobbyesp.imagingedge.domain.repository.CameraRepository
import io.ktor.client.HttpClient
import java.io.File
import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.data.remote.downloader.exceptions.DownloadException

/**
 * Default implementation of [CameraRepository] combining push, content browsing,
 * and image downloading capabilities.
 *
 * @param config      Global configuration for ImagingEdge (network, output directory, debug).
 * @param httpClient  Preconfigured Ktor HTTP client.
 * @param pushService Handles camera push (start/end transfer).
 * @param contentService Handles UPnP ContentDirectory browsing.
 * @param downloader  Saves remote files to local storage.
 */
class DefaultCameraRepository(
    private val config: ImagingEdgeConfig,
    private val httpClient: HttpClient,
    private val pushService: PushService = PushService(config, httpClient),
    private val contentService: ContentService = ContentService(config, httpClient),
    private val downloader: FileDownloader = FileDownloader(config.outputDir, httpClient)
) : CameraRepository {

    /** Retrieves the Push service descriptor XML from the camera. */
    override suspend fun getServiceInfo(): String =
        pushService.getServiceDescription()

    /** Sends the start-transfer command to the camera. */
    override suspend fun startTransfer() {
        pushService.startTransfer()
    }

    /** Sends the end-transfer command to the camera. */
    override suspend fun endTransfer() {
        pushService.endTransfer()
    }

    /**
     * Browses the contents of a UPnP directory.
     *
     * @param dirId        The UPnP container ID
     * @param downloadSize Preferred image size
     * @return List of directory entries (folders and items).
     */
    @Suppress("UNUSED_PARAMETER")
    override suspend fun getDirectoryContent(
        dirId: String,
        downloadSize: DownloadSize?
    ): List<DirectoryEntry> {
        val preferred = downloadSize ?: config.downloadSize
        return contentService.browseDirectory(objectId = dirId, preferredSize = preferred)
    }

    /**
     * Downloads the best available image for the given directory entry.
     */
    @Throws(DownloadException::class)
    override suspend fun downloadImage(
        entry: DirectoryEntry,
        preferredSize: DownloadSize?,
        listener: DownloadProgressListener?
    ): File {
        val size = preferredSize ?: config.downloadSize
        val image: ImageFile = contentService.parseImageInfo(entry, size)
        return downloader.download(image, listener)
    }

    /**
     * Downloads multiple images in sequence.
     */
    @Throws(DownloadException::class)
    suspend fun downloadImages(
        entries: List<DirectoryEntry>,
        preferredSize: DownloadSize? = null,
        listener: DownloadProgressListener? = null
    ): List<File> = entries.map { downloadImage(it, preferredSize, listener) }

}

