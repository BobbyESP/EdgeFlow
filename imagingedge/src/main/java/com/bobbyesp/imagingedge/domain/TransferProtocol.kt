package com.bobbyesp.imagingedge.domain

enum class TransferProtocol(
    val template: String,
) {
    UPNP("schemas-upnp-org"), SONY("schemas-sony-com")
}