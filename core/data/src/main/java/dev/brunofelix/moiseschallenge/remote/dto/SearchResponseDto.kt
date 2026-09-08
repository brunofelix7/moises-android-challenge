package dev.brunofelix.moiseschallenge.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseDto(
    @SerialName("resultCount")
    val resultCount: Int = 0,

    @SerialName("results")
    val results: List<ItunesResultDto> = emptyList()
)