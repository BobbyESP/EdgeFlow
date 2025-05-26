package com.bobbyesp.imagingedge.domain.model

data class ImageFile(
    val name: String,
    val url: String,
    val resolution: String?,
    val size: Long
)