package dev.brunofelix.moiseschallenge.core.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("track")
data class SongDto(
    @SerialName("trackId")
    val trackId: Long? = null,

    @SerialName("trackName")
    val trackName: String? = null,

    @SerialName("artistName")
    val artistName: String? = null,

    @SerialName("collectionId")
    val albumId: Long? = null,

    @SerialName("collectionName")
    val albumName: String? = null,

    @SerialName("artworkUrl100")
    val coverUrl: String? = null,

    @SerialName("previewUrl")
    val audioUrl: String? = null,

    @SerialName("trackTimeMillis")
    val durationMillis: Long? = null
)