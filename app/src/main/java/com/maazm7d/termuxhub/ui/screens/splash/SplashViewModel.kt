package com.maazm7d.termuxhub.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.TermuxHubApp

class SplashViewModel : ViewModel() {
    var ready = false
        private set

    fun load(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val minDelayMs = 1000L
            val start = System.currentTimeMillis()
            val success = TermuxHubApp.repository.refreshFromRemote()
            val elapsed = System.currentTimeMillis() - start
            if (elapsed < minDelayMs) delay(minDelayMs - elapsed)
            ready = true
            onComplete(success)
        }
    }
}
