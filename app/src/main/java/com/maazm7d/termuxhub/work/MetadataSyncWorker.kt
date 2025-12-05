package com.maazm7d.termuxhub.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maazm7d.termuxhub.data.repository.ToolRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class MetadataSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val repository: ToolRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result = try {
        val ok = repository.refreshFromRemote()
        if (ok) Result.success() else Result.retry()
    } catch (e: Exception) {
        Result.retry()
    }
}
