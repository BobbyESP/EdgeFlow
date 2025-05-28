package com.bobbyesp.imagingedge.data.remote.soap.requests

import com.bobbyesp.imagingedge.domain.SoapAction
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Browse", "urn:schemas-upnp-org:service:ContentDirectory:1", "u")
data class BrowseDirectoryRequest(
    @XmlElement(true) @XmlSerialName("ObjectID", "", "") val objectId: String,
    @XmlElement(true) @XmlSerialName(
        "BrowseFlag",
        "",
        ""
    ) val browseFlag: String = "BrowseDirectChildren",
    @XmlElement(true) @XmlSerialName("Filter", "", "") val filter: String = "*",
    @XmlElement(true) @XmlSerialName("StartingIndex", "", "") val startingIndex: Int = 0,
    @XmlElement(true) @XmlSerialName("RequestedCount", "", "") val requestedCount: Int = 100,
    @XmlElement(true) @XmlSerialName("SortCriteria", "", "") val sortCriteria: String = "",
) : SoapRequest {
    @Transient
    override val action = SoapAction.CONTENT_DIRECTORY
}