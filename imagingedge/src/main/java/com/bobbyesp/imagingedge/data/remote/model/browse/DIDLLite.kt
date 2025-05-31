package com.bobbyesp.imagingedge.data.remote.model.browse

import com.bobbyesp.imagingedge.data.remote.model.Container
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents the root element of a DIDL-Lite (Digital Item Declaration Language) document.
 *
 * This class is used to deserialize XML responses from UPnP (Universal Plug and Play) devices
 * that provide information about media content. It typically contains a list of containers
 * (folders) and/or items (media files).
 *
 * The `@Serializable` annotation indicates that this class can be serialized and deserialized
 * using Kotlinx Serialization.
 *
 * The `@XmlSerialName` annotation specifies the XML element name and namespace for this class.
 *  - `value = "DIDL-Lite"`: The local name of the XML element.
 *  - `namespace = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/"`: The namespace URI for the DIDL-Lite schema.
 *  - `prefix = ""`: Indicates that no prefix is used for this element in the XML.
 *
 * @property container A list of [Container] objects representing folders or collections of media.
 *                     This property is optional and will be null if no containers are present.
 *                     The `@XmlElement` annotation marks this property to be serialized/deserialized
 *                     as an XML element.
 * @property item A list of [Item] objects representing individual media files.
 *                This property is optional and will be null if no items are present.
 *                The `@XmlElement` annotation marks this property to be serialized/deserialized
 *                as an XML element.
 */
@Serializable
@XmlSerialName(
    value = "DIDL-Lite",
    namespace = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/",
    prefix = ""
)
data class DIDLLite(
    @XmlElement val container: List<Container>? = null,
    @XmlElement val item: List<Item>?      = null
)