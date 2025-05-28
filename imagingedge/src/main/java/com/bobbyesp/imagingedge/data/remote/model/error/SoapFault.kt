package com.bobbyesp.imagingedge.data.remote.model.error

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Fault", "http://schemas.xmlsoap.org/soap/envelope/", "s")
data class SoapFault(
    @XmlSerialName("faultcode") val faultCode: String,
    @XmlSerialName("faultstring") val faultString: String,
    @XmlElement(true) val detail: FaultDetail
)