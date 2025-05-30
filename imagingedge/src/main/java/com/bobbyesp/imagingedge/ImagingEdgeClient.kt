package com.bobbyesp.imagingedge

import com.bobbyesp.imagingedge.data.remote.DefaultCameraRepository
import com.bobbyesp.imagingedge.data.remote.downloader.exceptions.DownloadException
import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging

class ImagingEdgeClient(
    private val config: ImagingEdgeConfig,
    private val httpClient: HttpClient = HttpClient(OkHttp) {
        install(Logging) { level = if (config.debug) LogLevel.ALL else LogLevel.NONE }
    },
    private val repository: DefaultCameraRepository = DefaultCameraRepository(config, httpClient)
) {

    /**
     * Initializes the ImagingEdge client with the provided configuration.
     * This should be called before any other operations.
     */
    suspend fun connect() {
        repository.startTransfer()
    }

    /**
     * Ends the current transfer session with the camera.
     * This should be called after all operations are complete.
     */
    suspend fun disconnect() {
        repository.endTransfer()
    }

    /**
     * Lists the contents of a directory on the camera.
     * @param dirId The UPnP container ID to browse.
     * @param size Optional preferred size for images.
     * @return List of directory entries (folders and items).
     */
    suspend fun listDirectory(
        dirId: String, size: DownloadSize? = config.downloadSize
    ): List<DirectoryEntry> {
        return repository.getDirectoryContent(dirId, size)
    }

    /**
     * Downloads the specified image entry to local storage.
     * @param entry The image entry to download.
     * @param size Optional preferred size for the image.
     * @return The downloaded file.
     * @throws DownloadException On failure.
     */
    suspend fun downloadImage(
        entry: DirectoryEntry,
        size: DownloadSize? = config.downloadSize,
        downloadProgressListener: DownloadProgressListener? = null
    ) = repository.downloadImage(entry, size, downloadProgressListener)

    /**
     * Closes the HTTP client and releases resources.
     */
    fun close() {
        httpClient.close()
    }
}