package com.bobbyesp.imagingedge.data.remote.downloader

import com.bobbyesp.imagingedge.data.remote.downloader.exceptions.DownloadException
import com.bobbyesp.imagingedge.data.remote.downloader.exceptions.SizeMismatchException
import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.domain.model.ImageFile
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.plugins.onDownload
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentLength
import io.ktor.utils.io.readAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

private val logger = KotlinLogging.logger { }

class FileDownloader(
    private val outputRootDir: File,
    private val httpClient: HttpClient,
) {
    private val debug = true

    /**
     * Downloads an image file.
     *
     * This function attempts to download an image from the URL specified in the [image] object.
     * It first checks if the file already exists locally and if its size matches the expected size.
     * If so, it returns the existing file. Otherwise, it proceeds to download the file.
     *
     * During the download, it reports progress via the optional [listener].
     * If the download is successful, it returns the [File] object representing the downloaded image.
     *
     * @param image The [ImageFile] object containing the URL and expected size of the image.
     * @param listener An optional [DownloadProgressListener] to receive progress updates.
     * @return The [File] object of the downloaded image.
     * @throws DownloadException if an error occurs during the download process (e.g., network error, unexpected HTTP status).
     * @throws SizeMismatchException if the downloaded file size does not match the expected content length.
     */
    @Throws(DownloadException::class, SizeMismatchException::class)
    suspend fun download(
        image: ImageFile, listener: DownloadProgressListener? = null
    ): File = withContext(Dispatchers.IO) {
        val targetFile = getTargetFile(image)

        if (targetFile.exists() && image.size > 0 && targetFile.length() == image.size) {
            if (debug) logger.debug { "File already exists: ${targetFile.absolutePath}" }
            return@withContext targetFile
        }

        targetFile.parentFile?.mkdirs()

        try {
            val response: HttpResponse = httpClient.get(image.url) {
                if (debug) {
                    onDownload(listener = { bytesSentTotal, contentLength ->
                        val percentage = contentLength?.let {
                            if (it > 0) {
                                (bytesSentTotal * 100 / contentLength).toInt()
                            } else {
                                -1 // Unknown size
                            }
                        }
                        logger.debug {
                            "Downloading ($percentage% - $bytesSentTotal/$contentLength) ${image.url}"
                        }
                    })
                }
            }

            if (response.status != HttpStatusCode.OK) {
                throw DownloadException("Unexpected status code ${response.status.value} for ${image.url}")
            }

            val contentLength = response.contentLength() ?: -1
            val body = response.bodyAsChannel()

            if (debug) logger.debug {
                "Starting download of ${image.url} to ${targetFile.absolutePath} (expected size: $contentLength bytes)"
            }

            FileOutputStream(targetFile).use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var bytesRead: Int
                var totalRead = 0L

                while (true) {
                    bytesRead = body.readAvailable(buffer)
                    if (bytesRead <= 0) break
                    output.write(buffer, 0, bytesRead)
                    totalRead += bytesRead
                    listener?.onProgress(totalRead, contentLength)
                }

                if (contentLength > 0 && totalRead != contentLength) {
                    throw SizeMismatchException(
                        expectedSize = contentLength, actualSize = totalRead
                    )
                }
            }

        } catch (e: Exception) {
            throw DownloadException("Failed to download ${image.url}", e)
        }
        return@withContext targetFile
    }

    private fun getTargetFile(image: ImageFile): File {
        val subDir = File(outputRootDir, "images")
        return File(subDir, image.name)
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 16 * 1024 // 16 KB
    }
}
