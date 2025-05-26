package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("item", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Item(
    @XmlElement(false) val id: String,
    @XmlElement(false) val parentID: String,
    @XmlElement(false) val restricted: Boolean,

    @XmlElement @XmlSerialName("title", "http://purl.org/dc/elements/1.1/", "dc")
    val title: String,

    @XmlElement @XmlSerialName("class", "urn:schemas-upnp-org:metadata-1-0/upnp/", "upnp")
    val upnpClass: String,

    @XmlElement @XmlSerialName("contentType", "urn:schemas-sony-com:av", "av")
    val contentType: String,

    @XmlElement @XmlSerialName("date", "http://purl.org/dc/elements/1.1/", "dc")
    val date: String,

    @XmlElement(true) @XmlSerialName("res", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
    val res: List<Res>
)