package com.bobbyesp.imagingedge.data.remote.model.error

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Fault", "http://schemas.xmlsoap.org/soap/envelope/", "s")
data class SoapFault(
    @XmlElement(true)
    @XmlSerialName("faultcode", "", "") val faultCode: String,
    @XmlElement(true)
    @XmlSerialName("faultstring", "", "") val faultString: String,
    @XmlElement(true)
    val detail: FaultDetail
)