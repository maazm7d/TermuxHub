package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.repository.ToolRepository
import javax.inject.Inject

class GetStarsUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    suspend operator fun invoke(): Map<String, Int> =
        repository.fetchStars()
}
