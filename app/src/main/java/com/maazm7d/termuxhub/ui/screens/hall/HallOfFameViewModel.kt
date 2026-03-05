package com.maazm7d.termuxhub.ui.screens.hall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.HallOfFameRepository
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HallOfFameViewModel @Inject constructor(
    private val repository: HallOfFameRepository
) : ViewModel() {

    private val _members = MutableStateFlow<List<HallOfFameMember>>(emptyList())
    val members: StateFlow<List<HallOfFameMember>> = _members.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            _members.value = repository.loadMembers()
            _isLoading.value = false
        }
    }
}
