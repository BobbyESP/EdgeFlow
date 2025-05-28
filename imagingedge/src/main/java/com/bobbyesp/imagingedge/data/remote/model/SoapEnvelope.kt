package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(
    value = "Envelope",
    namespace = "http://schemas.xmlsoap.org/soap/envelope/",
    prefix = "s"
)
data class SoapEnvelope<T>(
    @XmlElement(false)
    @XmlSerialName(
        value = "encodingStyle",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/",
        prefix = "s"
    )
    val encodingStyle: String? = null,

    @XmlElement(true)
    @XmlSerialName(
        value = "Body",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/",
        prefix = "s"
    )
    val Body: SoapBody<T>
)

@Serializable
@XmlSerialName(
    value = "Body",
    namespace = "http://schemas.xmlsoap.org/soap/envelope/",
    prefix = "s"
)
data class SoapBody<T>(
    @XmlElement(true)
    val content: T
)