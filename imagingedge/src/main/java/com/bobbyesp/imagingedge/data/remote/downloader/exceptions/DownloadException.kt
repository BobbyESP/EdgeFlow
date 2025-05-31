package com.bobbyesp.imagingedge.data.remote.downloader.exceptions

/**
 * Generic exception for download errors.
 *
 * This exception is thrown when an error occurs during the download process.
 * It can be caused by a variety of factors, such as network errors, file not found errors, etc.
 *
 * It is recommended to catch this exception and handle it appropriately,
 * such as by displaying an error message to the user or retrying the download.
 *
 * @param message The error message.
 * @param cause The cause of the error.
 *
 * @see java.lang.Exception
 */
open class DownloadException: Exception {
    constructor() : super("An error occurred during the download process.")
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable) : super(message, cause)
}