package com.maazm7d.termuxhub.ui.screens.details

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.domain.model.ToolDetails
import com.maazm7d.termuxhub.domain.usecase.GetToolDetailsUseCase
import com.maazm7d.termuxhub.utils.TermuxIntentLauncher
import com.maazm7d.termuxhub.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

sealed class InstallState {
    data object Idle : InstallState()
    data object Launched : InstallState()
    data object TermuxMissing : InstallState()
    data object SecurityError : InstallState()
    data class Error(val message: String) : InstallState()
}

@HiltViewModel
class ToolDetailViewModel @Inject constructor(
    private val getToolDetailsUseCase: GetToolDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<ToolDetails>>(UiState.Loading)
    val uiState: StateFlow<UiState<ToolDetails>> = _uiState.asStateFlow()

    private val _installState = MutableStateFlow<InstallState>(InstallState.Idle)
    val installState: StateFlow<InstallState> = _installState.asStateFlow()

    fun loadToolDetails(toolId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val details = getToolDetailsUseCase(toolId)
                if (details != null) {
                    _uiState.value = UiState.Success(details)
                } else {
                    _uiState.value = UiState.Error("Tool not found")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to load tool details")
            }
        }
    }

    fun runInstall(context: Context, tool: ToolDetails) {
        if (!TermuxIntentLauncher.isTermuxInstalled(context)) {
            _installState.value = InstallState.TermuxMissing
            return
        }

        val commands = tool.installCommands
            .lines()
            .filter { it.isNotBlank() }
            .joinToString(" && ")

        try {
            TermuxIntentLauncher.run(context, commands)
            _installState.value = InstallState.Launched
        } catch (e: SecurityException) {
            Timber.e(e, "SecurityException while launching Termux intent")
            _installState.value = InstallState.SecurityError
        } catch (e: Exception) {
            Timber.e(e, "Error launching Termux intent")
            _installState.value = InstallState.Error(e.message ?: "Unknown error")
        }
    }

    fun resetInstallState() {
        _installState.value = InstallState.Idle
    }
}
