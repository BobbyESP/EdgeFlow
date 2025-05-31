package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents the response from the camera indicating the end of a transfer operation.
 * This is used in the context of the XPushList service, which is likely related to
 * transferring lists of images or other data from the camera.
 *
 * The `data object` keyword indicates that this is a singleton object, meaning there's
 * only one instance of it. This is appropriate for responses that don't carry any
 * specific data beyond their type.
 *
 * The `@Serializable` annotation makes this class usable with Kotlinx Serialization,
 * allowing it to be easily converted to and from formats like XML.
 *
 * The `@XmlSerialName` annotation specifies the XML element name and namespace
 * for this object when serialized or deserialized.
 * - `value = "X_TransferEndResponse"`: This is the local name of the XML element.
 * - `namespace = "urn:schemas-sony-com:service:XPushList:1"`: This is the XML namespace URI.
 * - `prefix = "u"`: This is the preferred XML namespace prefix.
 *
 * The `@XmlElement(true)` annotation indicates that this object should be serialized
 * as an XML element.
 */
@Serializable
@XmlSerialName(
    value = "X_TransferEndResponse",
    namespace = "urn:schemas-sony-com:service:XPushList:1",
    prefix = "u"
)
@XmlElement(true)
data object TransferEndResponse