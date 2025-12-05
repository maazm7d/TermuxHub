package com.maazm7d.termuxhub

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TermuxHubApp : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

    override fun onCreate() {
        super.onCreate()
        // NO MANUAL REPOSITORY CREATION HERE
        // Hilt will inject ToolRepository wherever needed
    }
}
