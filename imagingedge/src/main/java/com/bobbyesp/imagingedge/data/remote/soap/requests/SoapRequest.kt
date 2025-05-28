package com.bobbyesp.imagingedge.data.remote.soap.requests

import com.bobbyesp.imagingedge.domain.SoapAction
import kotlinx.serialization.Serializable

@Serializable
sealed interface SoapRequest {
  val action: SoapAction
}