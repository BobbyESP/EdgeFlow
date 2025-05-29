package com.bobbyesp.imagingedge.data.remote.model.browse

import com.bobbyesp.imagingedge.data.remote.soap.serializers.DIDLLiteFragmentSerializer
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("BrowseResponse", "urn:schemas-upnp-org:service:ContentDirectory:1", "u")
data class BrowseResponse(
    @XmlSerialName("Result", "", "")
    @XmlElement(true)
    @Serializable(with = DIDLLiteFragmentSerializer::class)
    val Result: DIDLLite,

    @XmlSerialName("NumberReturned", "", "")
    @XmlElement(true)
    val NumberReturned: Int,

    @XmlSerialName("TotalMatches", "", "")
    @XmlElement(true)
    val TotalMatches: Int,

    @XmlSerialName("UpdateID", "", "")
    @XmlElement(true)
    val UpdateID: Long
)