package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("Result", namespace = "", prefix = "")
data class ContentDirectoryResult(
    @XmlElement(true) val containers: List<Container>? = null,
    @XmlElement(true) val items: List<Item>? = null
)