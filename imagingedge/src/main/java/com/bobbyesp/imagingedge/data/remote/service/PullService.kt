package com.bobbyesp.imagingedge.data.remote.service

import com.bobbyesp.imagingedge.ImagingEdgeConfig
import io.ktor.client.HttpClient

class PullService(
    config: ImagingEdgeConfig, httpClient: HttpClient
) : CameraService(config, httpClient) {}