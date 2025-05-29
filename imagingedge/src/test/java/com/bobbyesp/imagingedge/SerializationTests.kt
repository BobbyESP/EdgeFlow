package com.bobbyesp.imagingedge

import com.bobbyesp.imagingedge.data.remote.model.browse.Browse
import com.bobbyesp.imagingedge.data.remote.model.browse.BrowseResponse
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer
import nl.adaptivity.xmlutil.XmlDeclMode
import nl.adaptivity.xmlutil.core.XmlVersion
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName
import org.junit.Test
import kotlin.reflect.KClass

class SerializationTests {
    @Suppress("UNCHECKED_CAST")
    val module = SerializersModule {
        polymorphic(Any::class) {
            subclass(BrowseResponse::class as KClass<BrowseResponse>, serializer())
        }
    }

    val xmlParser = XML(module) {
        xmlVersion = XmlVersion.XML10
        xmlDeclMode = XmlDeclMode.Charset
    }

    @Test
    fun `Test browse response serialization`() {
        val xml = """
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

        val encoded = xmlParser.decodeFromString<Envelope<BrowseResponse>>(xml)

        println(encoded)
    }
}

/**
 * The Envelope class is a very simple implementation of the SOAP Envelope (ignoring existence of headers). The
 * `@XmlSerialName` annotation specifies how the class is to be serialized, including namespace and prefix to try to use
 * (the serializer will try to reuse an existing prefix for the namespace if it already exists in the document).
 *
 * @property body `body` is a property that contains the body of the envelope. It merely wraps the data, but needs to exist
 *      for the purpose of generating the tag.
 * @param BODYTYPE SOAP is a generic protocol and the wrappers should not depend on a particular body data. That is why
 *      the type is parameterized (this works fine with Serialization).
 */
@Serializable
@XmlSerialName("Envelope", "http://schemas.xmlsoap.org/soap/envelope/", "S")
class Envelope<BODYTYPE> private constructor(
    private val body: Body<BODYTYPE>
) {

    /**
     * Actual constructor so users don't need to know about the body element
     */
    constructor(data: BODYTYPE) : this(Body(data))

    /**
     * Accessor to the data property that hides the body element.
     */
    val data: BODYTYPE get() = body.data

    override fun toString(): String {
        return "Envelope(body=$body)"
    }

    @Serializable
    @XmlSerialName(
        value = "encodingStyle",
        namespace = "http://schemas.xmlsoap.org/soap/envelope/"
    )
    private val encodingStyle: String = "http://schemas.xmlsoap.org/soap/encoding/"

    /**
     * The body class merely wraps a data element (the SOAP standard requires this to be a single element). There is no
     * need for this type to specify the serial name explicitly because:
     *  1. Body is a class, thus serialized as element. The name used is therefore (by default) determined by the name
     *     of the type (`Body`).
     *  2. The namespace (and prefix) used for a type is by default the namespace of the containing tag.
     *  3. Package names are normally elided in the naming
     *
     * The content of data is polymorphic to allow for different message types.
     *
     * @property data The data property contains the actual message content for the soap message.
     */
    @Serializable
    private data class Body<BODYTYPE>(@Polymorphic @XmlElement val data: BODYTYPE)

}