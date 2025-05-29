package com.bobbyesp.imagingedge.data.remote.soap.requests

import com.bobbyesp.imagingedge.domain.SoapAction
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlPolyChildren

@Serializable
@Polymorphic
sealed interface SoapRequest {
  val action: SoapAction
}