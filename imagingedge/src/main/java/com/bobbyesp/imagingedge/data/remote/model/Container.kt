package com.bobbyesp.imagingedge.data.remote.model

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents a container element in the DIDL-Lite metadata format.
 *
 * This data class is used to deserialize XML container elements that conform to the
 * "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/" namespace.
 *
 * @property id The unique identifier of the container.
 * @property parentID The identifier of the parent container.
 * @property restricted A boolean flag indicating if access to the container is restricted.
 * @property title The title of the container, using the Dublin Core "title" element.
 */
@Serializable
@XmlSerialName("container", "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "")
data class Container(
    @XmlElement(false) val id: String,
    @XmlElement(false) val parentID: String,
    @XmlElement(false) val restricted: Boolean,
    @XmlElement @XmlSerialName("title", "http://purl.org/dc/elements/1.1/", "dc")
    val title: String
)