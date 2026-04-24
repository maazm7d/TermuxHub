package com.maazm7d.termuxhub.domain.repository

import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import kotlinx.coroutines.flow.Flow

interface HallOfFameRepository {
    fun observeMembers(): Flow<List<HallOfFameMember>>
    suspend fun refresh(): Result<Unit>
}
