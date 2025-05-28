package com.bobbyesp.imagingedge.domain

/**
 * Represents a SOAP action that can be performed on a device.
 *
 * Each SOAP action has a [protocol], [service], and [action] associated with it.
 * The [toString] method returns a string representation of the SOAP action, which can be used in a SOAP request.
 *
 * Example:
 * ```kotlin
 * val soapAction = SoapAction.TRANSFER_START
 * println(soapAction) // "urn:schemas-sony-com:service:XPushList:1#X_TransferStart"
 * ```
 */
enum class SoapAction(
    val protocol: TransferProtocol,
    val service: String,
    val action: String,
) {
    TRANSFER_START(
        TransferProtocol.SONY,
        "XPushList",
        "X_TransferStart"
    ),
    TRANSFER_END(
        TransferProtocol.SONY,
        "XPushList",
        "X_TransferEnd"
    ),
    CONTENT_DIRECTORY(
        TransferProtocol.UPNP,
        "ContentDirectory",
        "Browse"
    );

    override fun toString(): String {
        return build(protocol.template, service, action)
    }

    fun toString(skipAction: Boolean = true): String {
        return build(protocol.template, service, if (skipAction) null else action)
    }

    companion object {
        fun build(
            protocol: String,
            service: String,
            action: String?,
        ) =
            "urn:$protocol:service:$service:1${action?.let { "#$it" } ?: ""}"
    }
}