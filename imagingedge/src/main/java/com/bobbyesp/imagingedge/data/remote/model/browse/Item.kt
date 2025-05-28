package com.bobbyesp.imagingedge.data.remote.model.browse

import com.bobbyesp.imagingedge.data.remote.model.Res
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents an item in the UPnP AV ContentDirectory service, typically a media file or a folder.
 * This class is designed to be serialized from and to XML using DIDL-Lite and related schemas.
 *
 * @property id The unique identifier for this item.
 * @property parentID The identifier of the parent container of this item.
 * @property restricted A boolean indicating if access to this item is restricted.
 * @property title The title of the item. Corresponds to the Dublin Core "title" element.
 * @property upnpClass The UPnP class of the item, indicating its type (e.g., "object.item.imageItem").
 * @property contentType The specific content type of the item, often a MIME type (e.g., "image/jpeg"). This uses the Sony AV namespace.
 * @property date The date associated with the item, typically its creation or modification date. Corresponds to the Dublin Core "date" element.
 * @property res A list of [com.bobbyesp.imagingedge.data.remote.model.Res] objects, representing the resources associated with this item (e.g., different versions or resolutions of a media file).
 */
@Serializable
@XmlSerialName("item", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Item(
    @XmlElement(false) val id: String,
    @XmlElement(false) val restricted: Boolean,
    @XmlElement(false) val parentID: String,

    @XmlElement @XmlSerialName("title", "http://purl.org/dc/elements/1.1/", "dc")
    val title: String,

    @XmlElement @XmlSerialName("class", "urn:schemas-upnp-org:metadata-1-0/upnp/", "upnp")
    val upnpClass: String,

    @XmlElement @XmlSerialName("contentType", "urn:schemas-sony-com:av", "av")
    val contentType: String,

    @XmlElement @XmlSerialName("date", "http://purl.org/dc/elements/1.1/", "dc")
    val date: String,

    @XmlElement @XmlSerialName("res", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
    val res: List<Res>
)