package com.bobbyesp.imagingedge.data.remote.model.browse

import com.bobbyesp.imagingedge.data.remote.soap.serializers.DIDLLiteFragmentSerializer
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Represents the response from a browse request to a UPnP ContentDirectory service.
 * This class encapsulates the results of a browse operation, including the list of items,
 * the number of items returned, the total number of matching items, and an update ID.
 *
 * @property result The [DIDLLite] object containing the list of items found.
 *                 This is serialized using [DIDLLiteFragmentSerializer].
 * @property resultCount The number of items returned in this specific response.
 * @property totalMatches The total number of items that match the browse criteria.
 *                       This might be different from [NumberReturned] if pagination is used.
 * @property lastUpdateId An identifier that changes whenever the content of the browsed directory changes.
 *                   This can be used to detect updates without re-fetching all items.
 */
@Serializable
@XmlSerialName("BrowseResponse", "urn:schemas-upnp-org:service:ContentDirectory:1", "u")
data class BrowseResponse(
    @XmlSerialName("Result", "", "")
    @XmlElement(true)
    @Serializable(with = DIDLLiteFragmentSerializer::class)
    val result: DIDLLite,

    @XmlSerialName("NumberReturned", "", "")
    @XmlElement(true)
    val resultCount: Int,

    @XmlSerialName("TotalMatches", "", "")
    @XmlElement(true)
    val totalMatches: Int,

    @XmlSerialName("UpdateID", "", "")
    @XmlElement(true)
    val lastUpdateId: Long
)