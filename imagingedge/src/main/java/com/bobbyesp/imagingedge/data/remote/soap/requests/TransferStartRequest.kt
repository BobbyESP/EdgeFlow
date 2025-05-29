package com.bobbyesp.imagingedge.data.remote.soap.requests

import com.bobbyesp.imagingedge.domain.SoapAction
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
@XmlSerialName("X_TransferStart", "urn:schemas-sony-com:service:XPushList:1", "u")
data object TransferStartRequest : SoapRequest {
    @Transient
    override val action = SoapAction.TransferStart
}