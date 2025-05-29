package com.bobbyesp.edgeflow.presentation.screens.home

import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bobbyesp.edgeflow.BuildConfig
import com.bobbyesp.imagingedge.ImagingEdgeClient
import com.bobbyesp.imagingedge.ImagingEdgeConfig
import com.bobbyesp.imagingedge.domain.DownloadProgressListener
import com.bobbyesp.imagingedge.domain.DownloadSize
import com.bobbyesp.imagingedge.domain.model.DirectoryEntry
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class ImagingEdgeViewModel : ViewModel() {

    data class UiState(
        val entries: List<DirectoryEntry> = emptyList(),
        val loading: Boolean = false,
        val error: String? = null,
        val downloadProgress: Float? = null,
        val connected: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    // Configuración y dependencias
    private val config = ImagingEdgeConfig(
        ip = "192.168.122.1", // TODO: Consider making IP and port configurable
        port = 64321,
        outputDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            OUTPUT_DIRECTORY_NAME
        ),
        debug = BuildConfig.DEBUG,
        downloadSize = DownloadSize.ORG
    )
    private val client = ImagingEdgeClient(config)

    private var loadEntriesJob: Job? = null
    private var downloadJob: Job? = null

    // Load directory entries from camera, tries PushRoot then PhotoRoot
    fun loadEntries() {
        loadEntriesJob?.cancel() // Cancelar trabajo anterior si existe
        loadEntriesJob = viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null, entries = emptyList()) }
            try {
                if (!_uiState.value.connected) {
                    try {
                        client.connect()
                        _uiState.update { it.copy(connected = true) }
                    } catch (connectError: Exception) {
                        Log.e(TAG, "Error connecting to client: ${connectError.message}", connectError)
                        _uiState.update {
                            it.copy(
                                error = connectError.localizedMessage ?: "Connection failed",
                                loading = false,
                                connected = false
                            )
                        }
                        return@launch // No continuar si la conexión falla
                    }
                }

                val entries = try {
                    client.listDirectory(PUSH_ROOT_DIR, config.downloadSize)
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to list $PUSH_ROOT_DIR, trying $PHOTO_ROOT_DIR. Error: ${e.message}")
                    // Asegurarse de que el cliente esté conectado o reconectar si es necesario antes de reintentar
                    // Esto es un ejemplo, la lógica exacta dependerá de cómo ImagingEdgeClient maneja los errores de conexión
                    if (!_uiState.value.connected) { // O alguna otra forma de verificar el estado de la conexión del cliente
                        client.connect() // Reintentar conexión
                        _uiState.update { it.copy(connected = true) }
                    }
                    client.listDirectory(PHOTO_ROOT_DIR, config.downloadSize)
                }
                _uiState.update { it.copy(entries = entries, loading = false) }
            } catch (e: CancellationException) {
                Log.i(TAG, "loadEntries job was cancelled.")
                // No es un error, simplemente la corutina fue cancelada
                // El estado de loading ya se maneja al inicio y en caso de éxito/error.
                // Si se cancela, podría ser útil resetear el estado de carga si no se completa.
                _uiState.update { it.copy(loading = false) }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading entries: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "Failed to load entries",
                        loading = false,
                        connected = false // Asumir desconexión si hay error cargando
                    )
                }
            }
        }
    }

    // Download selected entry with progress reporting
    fun downloadEntry(entry: DirectoryEntry) {
        downloadJob?.cancel() // Cancelar descarga anterior si existe
        downloadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(loading = true, error = null, downloadProgress = 0f)
            }
            try {
                // Opcional: verificar conexión aquí si es estrictamente necesario para descargar
                if (!_uiState.value.connected) {
                    Log.w(TAG, "Download attempt while not connected. Trying to connect.")
                    try {
                        client.connect()
                        _uiState.update { it.copy(connected = true) }
                    } catch (connectError: Exception) {
                        Log.e(TAG, "Connection failed before download: ${connectError.message}", connectError)
                        _uiState.update {
                            it.copy(
                                error = connectError.localizedMessage ?: "Connection required for download",
                                loading = false,
                                downloadProgress = null
                            )
                        }
                        return@launch
                    }
                }

                val downloadedFile = client.downloadImage(
                    entry,
                    config.downloadSize, // Se podría permitir un DownloadSize específico por descarga
                    downloadProgressListener = object : DownloadProgressListener {
                        override fun onProgress(bytesRead: Long, total: Long) {
                            val progress = if (total > 0) bytesRead.toFloat() / total else 0f
                            _uiState.update { it.copy(downloadProgress = progress) }
                        }
                    }
                )
                Log.i(TAG, "File downloaded successfully: ${downloadedFile.absolutePath}")
                _uiState.update { it.copy(loading = false, downloadProgress = null) }
            } catch (e: CancellationException) {
                Log.i(TAG, "downloadEntry job was cancelled.")
                _uiState.update { it.copy(loading = false, downloadProgress = null) } // Resetear progreso
            } catch (e: Exception) {
                Log.e(TAG, "Error downloading entry: ${e.message}", e)
                _uiState.update {
                    it.copy(
                        error = e.localizedMessage ?: "Download failed",
                        loading = false,
                        downloadProgress = null
                        // Considerar si el estado de 'connected' debe cambiar aquí
                    )
                }
            }
        }
    }

    suspend fun disconnect() {
        // Cancelar trabajos activos que dependen de la conexión
        loadEntriesJob?.cancel()
        downloadJob?.cancel()
        try {
            client.disconnect()
            _uiState.update { it.copy(connected = false, entries = emptyList(), error = null, loading = false, downloadProgress = null) }
        } catch (e: Exception) {
            Log.e(TAG, "Error disconnecting: ${e.message}", e)
            _uiState.update {
                it.copy(
                    error = e.localizedMessage ?: "Disconnect failed",
                    connected = false // Se intentó desconectar, así que se asume que ya no está conectado o el estado es indeterminado.
                    // Limpiar otros estados relevantes si es necesario
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "ViewModel cleared. Cancelling active jobs.")
        loadEntriesJob?.cancel()
        downloadJob?.cancel()
        // La desconexión del cliente en onCleared puede ser problemática si es una operación de red larga
        // y viewModelScope ya está cancelado.
        // Si es una operación rápida y no suspend, o si el cliente maneja su propio scope para limpieza, podría hacerse.
        // Ejemplo (si disconnect no fuera suspend o se manejara en otro scope):
        // if (_uiState.value.connected) {
        //     try {
        //         // Idealmente, client.disconnect() no debería ser suspend o debería usar un scope
        //         // que sobreviva al viewModelScope para operaciones críticas de limpieza.
        //         // runBlocking es una opción de último recurso y debe usarse con cuidado.
        //         // GlobalScope.launch { client.disconnect() } // Otra opción, pero con sus propias implicaciones.
        //         Log.i(TAG, "Attempting to disconnect client in onCleared.")
        //         // client.disconnect() // Si fuera síncrono
        //     } catch (e: Exception) {
        //         Log.e(TAG, "Error disconnecting client in onCleared: ${e.message}", e)
        //     }
        // }
    }

    companion object {
        private const val TAG = "ImagingEdgeViewModel"
        private const val PUSH_ROOT_DIR = "PushRoot"
        private const val PHOTO_ROOT_DIR = "PhotoRoot"
        private const val OUTPUT_DIRECTORY_NAME = "ImagingEdgeDemo"
    }
}