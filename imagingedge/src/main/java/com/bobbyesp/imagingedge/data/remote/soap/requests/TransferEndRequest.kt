package com.bobbyesp.imagingedge.data.remote.soap.requests

import com.bobbyesp.imagingedge.domain.SoapAction
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("X_TransferEnd", "urn:schemas-sony-com:service:XPushList:1", "u")
data class TransferEndRequest(
    @XmlElement(true)
    @XmlSerialName("ErrCode", "", "")
    val errCode: Int = 0,
) : SoapRequest {
    @Transient
    override val action = SoapAction.TransferEnd
}