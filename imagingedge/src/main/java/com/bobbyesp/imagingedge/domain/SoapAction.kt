package com.bobbyesp.imagingedge.domain

sealed class SoapAction(
    val protocolTemplate: String,
    val service: String,
    val actionName: String
) {
    val namespace: String
        get() = "urn:$protocolTemplate:service:$service:1"
    val action: String
        get() = actionName

    data object TRANSFER_START : SoapAction(TransferProtocol.SONY.template, "XPushList", "X_TransferStart")
    data object TRANSFER_END  : SoapAction(TransferProtocol.SONY.template, "XPushList", "X_TransferEnd")
    data object CONTENT_DIRECTORY : SoapAction(TransferProtocol.UPNP.template, "ContentDirectory", "Browse")

    class Custom(
        protocolTemplate: String,
        service: String,
        actionName: String
    ) : SoapAction(protocolTemplate, service, actionName)
}