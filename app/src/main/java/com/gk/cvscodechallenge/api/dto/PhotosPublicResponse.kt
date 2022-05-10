package com.gk.cvscodechallenge.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotosPublicResponse(
    @SerialName("title") val title: String?,
    @SerialName("link") val link: String?,
    @SerialName("description") val description: String?,
    @SerialName("modified") val modified: String?,
    @SerialName("generator") val generator: String?,
    @SerialName("items") val items: List<Item>?
) {
    companion object {
        fun createEmpty(
            title: String? = null,
            link: String? = null,
            description: String? = null,
            modified: String? = null,
            generator: String? = null,
            items: List<Item>? = null,
        ) = PhotosPublicResponse(
            title, link, description, modified, generator, items
        )
    }
}

@Serializable
data class Item(
    @SerialName("title") val title: String?,
    @SerialName("link") val link: String?,
    @SerialName("media") val media: Media?,
    @SerialName("date_taken") val dateTaken: String?,
    @SerialName("description") val description: String?,
    @SerialName("published") val published: String?,
    @SerialName("author") val author: String?,
    @SerialName("author_id") val authorId: String?,
    @SerialName("tags") val tags: String?
)

@Serializable
data class Media(
    @SerialName("m") val m: String?
)
