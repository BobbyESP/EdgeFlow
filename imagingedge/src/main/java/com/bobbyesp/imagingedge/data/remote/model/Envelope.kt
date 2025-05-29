package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Polymorphic
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement

/**
 * Represents a SOAP envelope.
 *
 * This class is used to serialize and deserialize SOAP messages. It is generic, allowing it to
 * wrap any type of data in the SOAP body. The `encodingStyle` property specifies the encoding
 * style used for the message, which defaults to "http://schemas.xmlsoap.org/soap/encoding/".
 *
 * @param BODY The type of data to be wrapped in the SOAP body.
 * @property encodingStyle The encoding style used for the message.
 * @property body The SOAP body, containing the actual data of the message.
 */
@Serializable
@XmlSerialName("Envelope", "http://schemas.xmlsoap.org/soap/envelope/", "s")
data class Envelope<BODY>(

    @XmlElement(false)
    @XmlSerialName(
        value = "encodingStyle",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/"
    )
    val encodingStyle: String = "http://schemas.xmlsoap.org/soap/encoding/",

    @XmlElement(true)
    val body: Body<BODY>
) {
    /**
     * Represents the body of a SOAP envelope.
     *
     * @param BODY The type of the data contained within the body.
     * @property data The actual data payload of the SOAP body. This field is polymorphic,
     * meaning it can hold different types of objects, and is serialized as an XML element.
     */
    @Serializable
    data class Body<BODY>(
        @Polymorphic @XmlElement(true)
        val data: BODY
    )
}