package com.maazm7d.termuxhub.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.maazm7d.termuxhub.data.repository.ToolRepository
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ToolRepository
) : ViewModel() {

    var ready = false
        private set

    fun load(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val minDelayMs = 1000L
            val start = System.currentTimeMillis()
            val success = repository.refreshFromRemote()
            val elapsed = System.currentTimeMillis() - start
            if (elapsed < minDelayMs) delay(minDelayMs - elapsed)
            ready = true
            onComplete(success)
        }
    }
}
