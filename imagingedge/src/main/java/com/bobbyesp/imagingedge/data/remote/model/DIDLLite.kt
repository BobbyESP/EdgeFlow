package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(
    value = "DIDL-Lite",
    namespace = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/",
    prefix = ""
)
data class DIDLLite(
    @XmlElement(true) val container: List<Container>? = null,
    @XmlElement(true) val item: List<Item>?      = null
)