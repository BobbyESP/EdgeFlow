package com.bobbyesp.imagingedge

import com.bobbyesp.imagingedge.data.remote.model.Container
import com.bobbyesp.imagingedge.data.remote.model.SoapBody
import com.bobbyesp.imagingedge.data.remote.model.SoapEnvelope
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import com.bobbyesp.imagingedge.data.remote.soap.SoapBodyBuilder
import com.bobbyesp.imagingedge.data.remote.soap.requests.BrowseDirectoryRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferStartRequest
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val xmlParser = XML {
        xmlVersion = XmlVersion.XML10
        xmlDeclMode = XmlDeclMode.Charset
    }

    @Test
    fun testXmlEncoding() {

        val result = xmlParser.encodeToString(
            SoapEnvelope(
                encodingStyle = "miau", body = SoapBody(
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
        val soapBodyBuilder = SoapBodyBuilder(xmlParser)

        val envelopeXml = soapBodyBuilder.buildSoapBody(TransferStartRequest)

        println("[Test] ->" + xmlParser.encodeToString<TransferStartRequest>(TransferStartRequest))

        println(envelopeXml)

        val secondText = soapBodyBuilder.buildSoapBody(
            BrowseDirectoryRequest(
                "PushRoot"
            )
        )

        println(secondText)
    }
}