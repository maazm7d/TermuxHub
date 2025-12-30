package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import javax.inject.Inject

class HallOfFameRepository @Inject constructor(
    private val metadataClient: MetadataClient
) {

    suspend fun loadMembers(): List<HallOfFameMember> {

        val indexResp = metadataClient.fetchHallOfFameIndex()
        if (!indexResp.isSuccessful) return emptyList()

        val members = indexResp.body()?.members ?: return emptyList()

        return members.map { dto ->

            val markdown = metadataClient
                .fetchHallOfFameMarkdown(dto.id)
                .body()
                .orEmpty()

            HallOfFameMember(
                id = dto.id,
                github = dto.github,
                speciality = dto.speciality,
                profileUrl = dto.profile,
                contribution = markdown
            )
        }
    }
}
