package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("container", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Container(
    @XmlElement(false) val id: String,
    @XmlElement(false) val parentID: String,
    @XmlElement(false) val restricted: Boolean,
    @XmlElement @XmlSerialName("title", "http://purl.org/dc/elements/1.1/", "dc")
    val title: String
)