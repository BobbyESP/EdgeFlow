package com.bobbyesp.imagingedge.data.remote.soap.serializers

import com.bobbyesp.imagingedge.data.remote.model.browse.DIDLLite
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.SerializersModule
import nl.adaptivity.xmlutil.serialization.XML

/**
 * A [kotlinx.serialization.KSerializer] for DIDLLite objects that handles them as XML fragments.
 *
 * This serializer is designed to work with DIDLLite objects that are embedded
 * within other XML structures, potentially as CDATA sections or with escaped
 * XML entities.
 *
 * **Deserialization:**
 * 1. Decodes the input string as is (which might contain CDATA or XML entities).
 * 2. Unescapes common XML entities (`&lt;`, `&gt;`, `&quot;`, `&apos;`, `&amp;`).
 * 3. Parses the resulting unescaped XML string back into a `DIDLLite` object using
 *    the standard `DIDLLite.serializer()`.
 *
 * **Serialization:**
 * 1. Serializes the `DIDLLite` object into an XML string fragment using the
 *    standard `DIDLLite.serializer()`.
 * 2. Encodes this fragment as a plain string.
 *
 * **Note:** When serializing, this implementation currently does **not** re-escape
 * the XML fragment into CDATA or XML entities. If the target system expects
 * escaped XML, this would need to be added.
 */
object DIDLLiteFragmentSerializer : KSerializer<DIDLLite> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DIDLLiteFragment", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): DIDLLite {
        // 1) Decode the input as a string (which may be a CDATA section or escaped XML)
        val raw   = decoder.decodeString()
        // 2) Unescape common XML entities
        val xml   = raw
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&quot;", "\"")
            .replace("&apos;", "'")
            .replace("&amp;", "&")
        // 3) Parse the XML string into a DIDLLite object
        val module = SerializersModule { /* register other entities if needed */ }
        val xmlUtil = XML(module) { autoPolymorphic = true }
        return xmlUtil.decodeFromString(DIDLLite.serializer(), xml)
    }

    override fun serialize(encoder: Encoder, value: DIDLLite) {
        // If you need to serialize as a CDATA section or escaped XML,
        val module = SerializersModule { }
        val xmlUtil = XML(module) { autoPolymorphic = true }
        val fragment = xmlUtil.encodeToString(DIDLLite.serializer(), value)
        encoder.encodeString(fragment)
    }
}