package dev.brunofelix.moiseschallenge.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("collection")
data class AlbumDto(
    @SerialName("collectionId")
    val collectionId: Long? = null,

    @SerialName("collectionName")
    val collectionName: String? = null,

    @SerialName("artistName")
    val artistName: String? = null,

    @SerialName("artworkUrl100")
    val coverUrl: String? = null,

    @SerialName("trackCount")
    val trackCount: Int? = null
) : ItunesResultDto()