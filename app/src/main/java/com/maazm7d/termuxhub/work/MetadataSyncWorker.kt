package com.maazm7d.termuxhub.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.maazm7d.termuxhub.TermuxHubApp

class MetadataSyncWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        return try {
            val ok = TermuxHubApp.repository.refreshFromRemote()
            if (ok) Result.success() else Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}