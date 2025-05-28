package com.bobbyesp.imagingedge

import com.bobbyesp.imagingedge.data.remote.model.Container
import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.soap.SoapBodyBuilder
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferStartRequest
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.serialization.XML
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testXmlEncoding() {
        val xmlEncoder = XML { }

        val result = xmlEncoder.encodeToString(
            SoapEnvelope(
                encodingStyle = "miau", Body = SoapBody(
                    Container(
                        id = "123", parentID = "0", restricted = false, title = "Test Container"
                    )
                )
            )
        )

        println(result)
    }

    @Test
    fun locura() {
        val xmlParser = XML { /* configure if needed */ }
        val soapBodyBuilder = SoapBodyBuilder(xmlParser)

        val envelopeXml = soapBodyBuilder.buildSoapBody(TransferStartRequest)

        println(envelopeXml)
    }
}