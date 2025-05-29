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
data class SoapReturnEnvelope<T>(
    // The prefix is automatically added by the library if a higher-level node already has it declared.
    @XmlElement(false)
    @XmlSerialName(
        value = "encodingStyle",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/"
    )
    val encodingStyle: String = "http://schemas.xmlsoap.org/soap/encoding/",

    @XmlElement
    @XmlSerialName(
        value = "Body",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/",
    )
    val body: T
)