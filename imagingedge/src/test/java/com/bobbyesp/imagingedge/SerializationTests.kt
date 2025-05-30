import com.bobbyesp.imagingedge.data.remote.model.Envelope
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import com.bobbyesp.imagingedge.data.remote.model.error.SoapFault
import com.bobbyesp.imagingedge.data.remote.soap.requests.BrowseDirectoryRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferEndRequest
import com.bobbyesp.imagingedge.data.remote.soap.requests.TransferStartRequest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML
import org.junit.Assert.*
import org.junit.Test

class SerializationTests {

    val module = SerializersModule {
        polymorphic(Any::class) {
            subclass(BrowseResponse::class, BrowseResponse.serializer())
            subclass(SoapFault::class, SoapFault.serializer())
            subclass(TransferStartRequest::class, TransferStartRequest.serializer())
            subclass(TransferEndRequest::class, TransferEndRequest.serializer())
            subclass(BrowseDirectoryRequest::class, BrowseDirectoryRequest.serializer())
        }
    }

    private val xml = XML(module) {
        xmlVersion = XmlVersion.XML10
        xmlDeclMode = XmlDeclMode.Charset
        autoPolymorphic = true
    }

    @Test
    fun testBrowseResponseDeserialization() {
        val xmlInput = """
            <?xml version="1.0"?>
            <s:Envelope
            	xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
            	<s:Body>
            		<u:BrowseResponse
            			xmlns:u="urn:schemas-upnp-org:service:ContentDirectory:1">
            			<Result>&lt;DIDL-Lite
            				xmlns:dc=&quot;http://purl.org/dc/elements/1.1/&quot;
            				xmlns=&quot;urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/&quot;
            				xmlns:upnp=&quot;urn:schemas-upnp-org:metadata-1-0/upnp/&quot;
            				xmlns:dlna=&quot;urn:schemas-dlna-org:metadata-1-0/&quot;
            				xmlns:arib=&quot;urn:schemas-arib-or-jp:elements-1-0/&quot;
            				xmlns:av=&quot;urn:schemas-sony-com:av&quot;&gt;
            &lt;item id=&quot;04_02_0000150688_000005_000003_000000&quot; restricted=&quot;1&quot; parentID=&quot;03_01_0000150688_000005_000000_000000&quot;&gt;&lt;dc:title&gt;DSC00636.JPG&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.imageItem.photo&lt;/upnp:class&gt;&lt;av:contentType&gt;STILL&lt;/av:contentType&gt;&lt;dc:date&gt;2025-05-28T21:12:44&lt;/dc:date&gt;&lt;res protocolInfo=&quot;http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG;DLNA.ORG_CI=1&quot;&gt;http://192.168.122.1:60151/LRG_DSC00636.JPG?%2104%5f02%5f0000150688%5f000005%5f000003%5f000000%21http%2dget%3a%2a%3aimage%2fjpeg%3aDLNA%2eORG%5fPN%3dJPEG%5fLRG%3bDLNA%2eORG%5fCI%3d1%21%21%21%21%21&lt;/res&gt;&lt;res protocolInfo=&quot;http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_SM;DLNA.ORG_CI=1&quot;&gt;http://192.168.122.1:60151/SM_DSC00636.JPG?%2104%5f02%5f0000150688%5f000005%5f000003%5f000000%21http%2dget%3a%2a%3aimage%2fjpeg%3aDLNA%2eORG%5fPN%3dJPEG%5fSM%3bDLNA%2eORG%5fCI%3d1%21%21%21%21%21&lt;/res&gt;&lt;res protocolInfo=&quot;http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN;DLNA.ORG_CI=1&quot;&gt;http://192.168.122.1:60151/TN_DSC00636.JPG?%2104%5f02%5f0000150688%5f000005%5f000003%5f000000%21http%2dget%3a%2a%3aimage%2fjpeg%3aDLNA%2eORG%5fPN%3dJPEG%5fTN%3bDLNA%2eORG%5fCI%3d1%21%21%21%21%21&lt;/res&gt;&lt;res protocolInfo=&quot;http-get:*:image/jpeg:*&quot; size=&quot;7014659&quot; resolution=&quot;4896x3672&quot;&gt;http://192.168.122.1:60151/ORG_DSC00636.JPG?%2104%5f02%5f0000150688%5f000005%5f000003%5f000000%21http%2dget%3a%2a%3aimage%2fjpeg%3a%2a%217014659%21%21%21%21&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;

            			</Result>
            			<NumberReturned>1</NumberReturned>
            			<TotalMatches>1</TotalMatches>
            			<UpdateID>2089222859</UpdateID>
            		</u:BrowseResponse>
            	</s:Body>
            </s:Envelope>

        """.trimIndent()

        val envelope = xml.decodeFromString<Envelope<BrowseResponse>>(xmlInput)

        assertNotNull("Envelope should not be null", envelope)
        assertNotNull("Envelope body should not be null", envelope.body)

        val browse = envelope.body.data
        assertNotNull("BrowseResponse data should not be null", browse)

        assertEquals("NumberReturned should be 1", 1, browse.NumberReturned)
        assertEquals("TotalMatches should be 1", 1, browse.TotalMatches)
        assertEquals("UpdateID should match", 2089222859L, browse.UpdateID)

        val didl = browse.Result
        assertNotNull("DIDL-Lite Result should not be null", didl)
        assertNotNull("DIDL items should not be null", didl.item)
        assertTrue("Should have at least one item", didl.item!!.isNotEmpty())

        val item = didl.item.first()
        assertEquals("Item ID should match", "04_02_0000150688_000005_000003_000000", item.id)
        assertEquals("Item title should match", "DSC00636.JPG", item.title)
        assertTrue("Should have resources", item.res.isNotEmpty())
    }

    @Test
    fun testBrowseDirectoryRequestSerialization() {
        val request = BrowseDirectoryRequest("PushRoot")

        val xmlString = xml.encodeToString(
            Envelope(
                body = Envelope.Body(
                    data = request
                )
            )
        )

        println("Encoded BrowseDirectoryRequest:\n$xmlString")

        assertNotNull("Encoded XML string should not be null", xmlString)
        assertTrue("XML should contain s:Envelope", xmlString.contains("<s:Envelope"))
        assertTrue("XML should contain s:Body", xmlString.contains("<s:Body"))
        assertTrue("XML should contain u:Browse tag", xmlString.contains("<u:Browse"))
        assertTrue(
            "XML should contain ObjectID",
            xmlString.contains("<ObjectID>PushRoot</ObjectID>")
        )
        assertTrue(
            "XML should contain BrowseFlag",
            xmlString.contains("<BrowseFlag>BrowseDirectChildren</BrowseFlag>")
        )
        assertTrue("XML should close u:Browse tag", xmlString.contains("</u:Browse>"))
        assertTrue("XML should close s:Envelope", xmlString.contains("</s:Envelope>"))
    }

    @Test
    fun testSoapFaultDeserialization() {
        val faultXml = """
            <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/" s:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
                <s:Body>
                    <s:Fault>
                        <faultcode>s:Client</faultcode>
                        <faultstring>UPnPError</faultstring>
                        <detail>
                            <UPnPError xmlns="urn:schemas-upnp-org:control-1-0">
                                <errorCode>506</errorCode>
                                <errorDescription>Action Failed</errorDescription>
                            </UPnPError>
                        </detail>
                    </s:Fault>
                </s:Body>
            </s:Envelope>
        """.trimIndent()

        val envelope = xml.decodeFromString<Envelope<SoapFault>>(faultXml)

        assertNotNull("Envelope should not be null", envelope)
        assertNotNull("Envelope body should not be null", envelope.body)
        assertNotNull("Envelope body data should not be null", envelope.body.data)

        assertTrue("Body data should be SoapFault", true)

        val soapFault = envelope.body.data
        assertEquals("Faultcode should be s:Client", "s:Client", soapFault.faultCode)
        assertEquals("Faultstring should be UPnPError", "UPnPError", soapFault.faultString)

        assertNotNull("Fault detail should not be null", soapFault.detail)
        val upnpError = soapFault.detail.error
        assertNotNull("UPnPError should not be null", upnpError)
        assertEquals("UPnPError errorCode should be 506", 506, upnpError.errorCode)
        assertEquals(
            "UPnPError errorDescription should be Action Failed",
            "Action Failed",
            upnpError.errorDescription
        )
    }
}
