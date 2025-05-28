package com.bobbyesp.imagingedge.data.remote.model.browse

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(
    value = "Browse",
    namespace = "urn:schemas-upnp-org:service:ContentDirectory:1",
    prefix = "u"
)
data class Browse(
    @XmlElement
    @XmlSerialName("ObjectID", "", "")
    val objectId: String,
    @XmlElement
    @XmlSerialName("BrowseFlag", "", "")
    val browseFlag: String,
    @XmlElement
    @XmlSerialName("Filter", "", "")
    val filter: String = "*",
    @XmlElement
    @XmlSerialName("StartingIndex", "", "")
    val startingIndex: Int,
    @XmlElement
    @XmlSerialName("RequestedCount", "", "")
    val requestedCount: Int,
    @XmlElement
    @XmlSerialName("SortCriteria", "", "")
    val sortCriteria: String = ""
)
