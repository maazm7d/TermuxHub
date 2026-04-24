package com.maazm7d.termuxhub.domain.usecase

import com.maazm7d.termuxhub.domain.repository.ToolRepository
import javax.inject.Inject

class RefreshToolsUseCase @Inject constructor(
    private val repository: ToolRepository
) {
    suspend operator fun invoke(): Boolean =
        repository.refreshFromRemote()
}
