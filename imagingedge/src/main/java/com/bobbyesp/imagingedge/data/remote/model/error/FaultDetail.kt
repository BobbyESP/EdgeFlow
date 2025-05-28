package com.bobbyesp.imagingedge.data.remote.model.error

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("detail", "", "")
data class FaultDetail(
    @XmlElement(true)
    @XmlSerialName("UPnPError", "urn:schemas-upnp-org:control-1-0", "")
    val error: UPnPError
)