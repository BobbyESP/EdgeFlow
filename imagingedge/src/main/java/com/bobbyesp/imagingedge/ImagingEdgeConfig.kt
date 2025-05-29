package com.bobbyesp.imagingedge

import com.bobbyesp.imagingedge.domain.DownloadSize
import java.io.File

data class ImagingEdgeConfig(
    val ip: String = "192.168.122.1",
    val port: Int = 64321,
    val outputDir: File,
    val debug: Boolean = true,
    val downloadSize: DownloadSize? = null
) {
    val baseUrl: String
        get() = "http://$ip:$port"
}