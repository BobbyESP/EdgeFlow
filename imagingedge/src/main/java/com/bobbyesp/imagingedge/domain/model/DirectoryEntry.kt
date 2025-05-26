package com.bobbyesp.imagingedge.domain.model

data class DirectoryEntry(
    val id: String? = null,
    val title: String,
    val isDirectory: Boolean,
    val rawXml: Any? = null
)