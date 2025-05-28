package com.bobbyesp.imagingedge.data.remote.downloader.exceptions

open class DownloadException: Exception {
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}