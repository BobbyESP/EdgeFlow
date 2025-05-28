package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName(
    value = "X_TransferEndResponse",
    namespace = "urn:schemas-sony-com:service:XPushList:1",
    prefix = "u"
)
@XmlElement(true)
data object TransferEndResponse