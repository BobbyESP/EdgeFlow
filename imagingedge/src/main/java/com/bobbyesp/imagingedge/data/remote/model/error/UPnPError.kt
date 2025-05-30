package com.bobbyesp.imagingedge.data.remote.model.error

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("UPnPError", "urn:schemas-upnp-org:control-1-0", "")
data class UPnPError(
    @XmlElement(true) @XmlSerialName("errorCode", "", "") val errorCode: Int,
    @XmlElement(true) @XmlSerialName("errorDescription", "", "") val errorDescription: String
)