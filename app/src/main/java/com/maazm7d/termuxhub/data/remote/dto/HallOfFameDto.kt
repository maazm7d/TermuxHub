package com.maazm7d.termuxhub.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HallOfFameIndexDto(
    val members: List<HallOfFameMemberDto>
)

@JsonClass(generateAdapter = true)
data class HallOfFameMemberDto(
    val id: String,
    val github: String,
    val speciality: String,
    val profile: String
)
