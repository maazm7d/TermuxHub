package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.repository.HallOfFameRepository
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHallOfFameMembersUseCase @Inject constructor(
    private val repository: HallOfFameRepository
) {
    operator fun invoke(): Flow<List<HallOfFameMember>> =
        repository.observeMembers()
}
