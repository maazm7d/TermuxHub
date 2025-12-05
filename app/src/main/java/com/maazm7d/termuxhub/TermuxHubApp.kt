package com.maazm7d.termuxhub

import android.app.Application
import androidx.work.Configuration
import com.maazm7d.termuxhub.data.repository.ToolRepositoryImpl
import com.maazm7d.termuxhub.data.local.db.AppDatabase
import com.maazm7d.termuxhub.data.remote.ApiClient
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TermuxHubApp : Application(), Configuration.Provider {

    companion object {
        lateinit var repository: ToolRepositoryImpl
            private set
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Database & API + Repository
        val db = AppDatabase.getInstance(this)
        val api = ApiClient.api
        repository = ToolRepositoryImpl(
            toolDao = db.toolDao(),
            api = api
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
