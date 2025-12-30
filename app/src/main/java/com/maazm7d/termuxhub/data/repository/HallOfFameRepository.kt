package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import javax.inject.Inject

class HallOfFameRepository @Inject constructor(
    private val metadataClient: MetadataClient
) {

    suspend fun loadMembers(): List<HallOfFameMember> {
        val index = metadataClient.apiService.getHallOfFameIndex()
        if (!index.isSuccessful) return emptyList()

        return index.body()?.members?.map { dto ->
            val md = metadataClient.apiService
                .getHallOfFameMarkdown(dto.id)
                .body()
                .orEmpty()

            HallOfFameMember(
                id = dto.id,
                github = dto.github,
                speciality = dto.speciality,
                profileUrl = dto.profile,
                contribution = md
            )
        } ?: emptyList()
    }
}
