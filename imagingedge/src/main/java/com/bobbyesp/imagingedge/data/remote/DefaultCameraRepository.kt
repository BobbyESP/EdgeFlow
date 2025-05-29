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
    override suspend fun getDirectoryContent(
        dirId: String,
        downloadSize: DownloadSize?
    ): List<DirectoryEntry> {
        val preferred = downloadSize ?: config.downloadSize
        return contentService.browseDirectory(objectId = dirId, preferredSize = preferred)
    }

    /**
     * Downloads the best available image for the given directory entry.
     *
     * @param entry The directory entry representing the image to download.
     * @param preferredSize The preferred size of the image to download. If null, the size specified in the [ImagingEdgeConfig] will be used.
     * @param listener An optional listener to track the download progress.
     * @return The downloaded [File].
     * @throws DownloadException If an error occurs during the download process.
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
     *
     * @param entries List of [DirectoryEntry] objects representing the images to download.
     * @param preferredSize Optional [DownloadSize] to specify the desired image size. If null, the global config value is used.
     * @param listener Optional [DownloadProgressListener] to receive progress updates.
     * @return A list of [File] objects representing the downloaded images.
     * @throws DownloadException if any error occurs during the download process for any of the images.
     */
    @Throws(DownloadException::class)
    suspend fun downloadImages(
        entries: List<DirectoryEntry>,
        preferredSize: DownloadSize? = null,
        listener: DownloadProgressListener? = null
    ): List<File> = entries.map { downloadImage(it, preferredSize, listener) }

}

