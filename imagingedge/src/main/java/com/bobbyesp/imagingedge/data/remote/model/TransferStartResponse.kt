package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents the response from the camera when a transfer start request is sent.
 * This is an empty object as the camera does not return any data in the response body.
 * The presence of this response indicates that the transfer start request was successful.
 */
@Serializable
@XmlSerialName(
    value = "X_TransferStartResponse",
    namespace = "urn:schemas-sony-com:service:XPushList:1",
    prefix = "u"
)
@XmlElement(true)
data object TransferStartResponse