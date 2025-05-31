package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

/**
 * Represents a resource element in a DIDL-Lite document, typically used in UPnP (Universal Plug and Play) scenarios.
 * This class encapsulates information about a media resource, such as its URL, resolution, size, and protocol information.
 *
 * @property resolution The resolution of the media resource (e.g., "1920x1080"). This is optional.
 * @property size The size of the media resource in bytes. This is optional.
 * @property protocolInfo Information about the protocol used to access the media resource (e.g., "http-get:*:image/jpeg:*"). This is optional.
 * @property url The URL from which the media resource can be accessed. This is a required field.
 */
@Serializable
@XmlSerialName("res", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Res(
    @XmlElement(false) val protocolInfo: String? = null,
    @XmlElement(false) val size: Long?       = null,
    @XmlElement(false) val duration: String? = null,
    @XmlElement(false) val resolution: String? = null,
    @XmlElement(false) @XmlSerialName("avc_profile") val avcProfile: String? = null,
    @XmlElement(false) @XmlSerialName("avc_level") val avcLevel: String? = null,
    @XmlElement(false) @XmlSerialName("file_name") val filename: String? = null,
    @XmlValue           val url: String
)