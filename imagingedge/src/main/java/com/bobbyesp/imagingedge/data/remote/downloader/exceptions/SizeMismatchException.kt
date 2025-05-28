package com.bobbyesp.imagingedge.data.remote.downloader.exceptions

class SizeMismatchException: DownloadException {
    constructor(expectedSize: Long, actualSize: Long) :
        super("Size mismatch: expected $expectedSize bytes, but got $actualSize bytes")
}