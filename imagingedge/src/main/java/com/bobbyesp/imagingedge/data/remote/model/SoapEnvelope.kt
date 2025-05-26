package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Envelope", "http://schemas.xmlsoap.org/soap/envelope/", "s")
data class SoapEnvelope<T>(
    @XmlElement(true)
    val Body: SoapBody<T>
)

@Serializable
@XmlSerialName("Body", "http://schemas.xmlsoap.org/soap/envelope/", "s")
data class SoapBody<T>(
    @XmlElement(true)
    val content: T
)