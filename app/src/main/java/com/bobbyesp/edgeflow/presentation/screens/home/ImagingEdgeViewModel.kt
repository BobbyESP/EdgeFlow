package com.bobbyesp.edgeflow.presentation.screens.home

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.data.remote.DefaultCameraRepository
import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ImagingEdgeViewModel : ViewModel() {

    data class UiState(
        val entries: List<DirectoryEntry> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null,
        val downloadProgress: Float? = null
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // Configuración y dependencias
    private val config = ImagingEdgeConfig(
        ip = "192.168.122.1", port = 64321, outputDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "ImagingEdgeDemo"
        ), debug = true, downloadSize = DownloadSize.BEST
    )
    private val httpClient = HttpClient(Android)
    private val repository = DefaultCameraRepository(
        config, httpClient
    )

    // Load directory entries from camera, tries PushRoot then PhotoRoot
    fun loadEntries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            try {
                repository.startTransfer()
                val entries = try {
                    repository.getDirectoryContent("PushRoot")
                } catch (_: Exception) {
                    repository.getDirectoryContent("PhotoRoot")
                }
                repository.endTransfer()
                _uiState.value = _uiState.value.copy(entries = entries, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.localizedMessage, loading = false)
            }
        }
    }

    // Download selected entry with progress reporting
    fun downloadEntry(entry: DirectoryEntry) {
        viewModelScope.launch {
            _uiState.value =
                _uiState.value.copy(loading = true, error = null, downloadProgress = 0f)
            try {
                repository.downloadImage(
                    entry,
                    config.downloadSize,
                    listener = object : DownloadProgressListener {
                        override fun onProgress(bytesRead: Long, total: Long) {
                            val progress = if (total > 0) bytesRead.toFloat() / total else 0f
                            _uiState.value = _uiState.value.copy(downloadProgress = progress)
                        }
                    })
                _uiState.value = _uiState.value.copy(loading = false, downloadProgress = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.localizedMessage, loading = false, downloadProgress = null
                )
            }
        }
    }
}


