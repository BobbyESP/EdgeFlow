package com.bobbyesp.imagingedge.data.remote.model.browse

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("BrowseResponse", "urn:schemas-upnp-org:service:ContentDirectory:1", "u")
data class BrowseResponse(
    // The old "ContentDirectoryResult"
    @XmlElement(true)
    val Result: DIDLLite,
    @XmlSerialName("NumberReturned")
    val NumberReturned: Int,
    @XmlSerialName("TotalMatches")
    val TotalMatches: Int,
    @XmlSerialName("UpdateID")
    val UpdateID: Long
)