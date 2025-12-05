package com.maazm7d.termuxhub

import android.app.Application
import androidx.work.Configuration
import com.maazm7d.termuxhub.data.local.AppDatabase
import com.maazm7d.termuxhub.data.remote.MetadataClient
import com.maazm7d.termuxhub.data.repository.ToolsRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class TermuxHubApp : Application(), Configuration.Provider {

    companion object {
        // publicly accessible singletons used across the app (avoid null checks)
        lateinit var instance: TermuxHubApp
            private set

        lateinit var database: AppDatabase
            private set

        lateinit var metadataClient: MetadataClient
            private set

        lateinit var repository: ToolsRepository
            private set

        // app scope for root-level coroutines if needed
        val appScope = CoroutineScope(SupervisorJob())
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // initialize local DB
        database = AppDatabase.getDatabase(this)

        // initialize MetadataClient (Retrofit + OkHttp), companion create() exists in your code
        metadataClient = MetadataClient.create()

        // initialize repository using existing ToolsRepository constructor
        // (constructor signature seen earlier: (toolDao, metadataClient, assetsFileName, appContext))
        repository = ToolsRepository(
            toolDao = database.toolDao(),
            metadataClient = metadataClient,
            assetsFileName = "metadata.json",
            appContext = this
        )
    }

    // WorkManager configuration (as a property implementation)
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
}
