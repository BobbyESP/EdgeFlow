package com.bobbyesp.edgeflow.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ImagingEdgeViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("ImagingEdge Demo") }) }) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.loadEntries() }, modifier = Modifier.weight(1f)
                ) { Text("Explorar") }
                Button(
                    onClick = { /* Quizá refrescar download dir list */ },
                    enabled = false,
                    modifier = Modifier.weight(1f)
                ) { Text("Descargas") }
            }

            Spacer(Modifier.height(16.dp))

            if (uiState.loading) {
                CircularProgressIndicator()
            }

            uiState.error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            LazyColumn(Modifier.fillMaxWidth()) {
                items(uiState.entries) { entry ->
                    val icon = if (entry.isDirectory) "📁" else "🖼️"
                    Row(Modifier.fillMaxWidth().clickable {
                            if (!entry.isDirectory) {
                                viewModel.downloadEntry(entry)
                            }
                        }.padding(vertical = 8.dp)) {
                        Text(icon, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.width(8.dp))
                        Text(entry.title, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}