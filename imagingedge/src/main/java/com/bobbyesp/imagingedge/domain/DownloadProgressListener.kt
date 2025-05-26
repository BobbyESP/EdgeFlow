package com.bobbyesp.imagingedge.domain

interface DownloadProgressListener {
    fun onProgress(bytesRead: Long, total: Long)
}