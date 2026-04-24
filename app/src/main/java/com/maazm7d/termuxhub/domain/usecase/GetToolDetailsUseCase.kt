package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.repository.ToolRepository
import com.maazm7d.termuxhub.domain.model.ToolDetails
import javax.inject.Inject

class GetToolDetailsUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    suspend operator fun invoke(id: String): ToolDetails? =
        repository.getToolDetails(id)
}
