package com.maazm7d.termuxhub.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.TermuxHubApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.domain.mapper.toDomain

class SplashViewModel : ViewModel() {

    var ready = false
        private set

    /**
     * Load metadata and prepare DB. We ensure splash lasts at least `minDelayMs`.
     * onFinished will be called by the UI when ready == true
     */
    fun load(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val minDelayMs = 1000L // minimum splash visible time
            val start = System.currentTimeMillis()
            val success = TermuxHubApp.repository.refreshFromRemote()
            val elapsed = System.currentTimeMillis() - start
            if (elapsed < minDelayMs) {
                delay(minDelayMs - elapsed)
            }
            ready = true
            onComplete(success)
        }
    }
}