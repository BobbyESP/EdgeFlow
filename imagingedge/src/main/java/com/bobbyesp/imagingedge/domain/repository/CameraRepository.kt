package com.bobbyesp.imagingedge.domain.repository

import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import com.bobbyesp.imagingedge.data.remote.downloader.exceptions.DownloadException
import java.io.File

interface CameraRepository {

    /**
     * Retrieves the Push service descriptor XML from the camera.
     * This is used to understand how to communicate with the camera's push service.
     * @return The service descriptor XML as a String.
     */
    suspend fun getServiceInfo(): String

    /**
     * Sends the start-transfer command to the camera.
     * This is typically called before downloading images.
     */
    suspend fun startTransfer()

    /**
     * Sends the end-transfer command to the camera.
     * This is typically called after a series of image downloads.
     */
    suspend fun endTransfer()
    /**
     * Retrieves the content of a UPnP directory.
     * @param dirId The UPnP container ID.
     * @param downloadSize Optional preferred image size for future use.
     * @return List of directory entries (folders and items).
     */
    suspend fun getDirectoryContent(
        dirId: String,
        downloadSize: DownloadSize? = null
    ): List<DirectoryEntry>

    /**
     * Downloads the given image directory entry to local storage.
     * @param entry The image entry to download.
     * @param preferredSize Optional size preference.
     * @param listener Optional download progress listener.
     * @return The downloaded file.
     * @throws DownloadException On failure.
     */
    suspend fun downloadImage(
        entry: DirectoryEntry,
        preferredSize: DownloadSize? = null,
        listener: DownloadProgressListener? = null
    ): File
}

