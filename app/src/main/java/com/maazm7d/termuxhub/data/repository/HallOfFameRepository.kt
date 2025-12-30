package com.maazm7d.termuxhub.data.repository

import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import javax.inject.Inject

class HallOfFameRepository @Inject constructor(
    private val metadataClient: MetadataClient
) {

    suspend fun loadMembers(): List<HallOfFameMember> {
        return try {

            val indexResp = metadataClient.fetchHallOfFameIndex()
            if (!indexResp.isSuccessful) return emptyList()

            val members = indexResp.body()?.members ?: return emptyList()

            members.map { dto ->
                val markdown = try {
                    metadataClient
                        .fetchHallOfFameMarkdown(dto.id)
                        .body()
                        .orEmpty()
                } catch (e: Exception) {
                    "" // markdown optional
                }

                HallOfFameMember(
                    id = dto.id,
                    github = dto.github,
                    speciality = dto.speciality,
                    profileUrl = dto.profile,
                    contribution = markdown
                )
            }

        } catch (e: Exception) {
            // 🔥 THIS prevents crash when internet is OFF
            e.printStackTrace()
            emptyList()
        }
    }
}
