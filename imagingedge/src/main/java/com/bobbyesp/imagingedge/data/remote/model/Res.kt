package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import nl.adaptivity.xmlutil.serialization.XmlValue

@Serializable
@XmlSerialName("res", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Res(
    @XmlElement(false) val resolution: String? = null,
    @XmlElement(false) val size: Long?       = null,
    @XmlElement(false) val protocolInfo: String? = null,
    @XmlValue           val url: String
)