package com.bobbyesp.imagingedge.data.remote.downloader.exceptions

/**
 * Exception thrown when the downloaded file size does not match the expected size.
 *
 * @param expectedSize The expected size of the file in bytes.
 * @param actualSize The actual size of the downloaded file in bytes.
 */
class SizeMismatchException: DownloadException {
    constructor(expectedSize: Long, actualSize: Long) :
        super("Size mismatch: expected $expectedSize bytes, but got $actualSize bytes")
}