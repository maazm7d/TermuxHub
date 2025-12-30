package com.maazm7d.termuxhub.ui.screens.hall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maazm7d.termuxhub.data.repository.HallOfFameRepository
import com.maazm7d.termuxhub.domain.model.HallOfFameMember
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State

@HiltViewModel
class HallOfFameViewModel @Inject constructor(
    private val repository: HallOfFameRepository
) : ViewModel() {

    private val _members = mutableStateOf<List<HallOfFameMember>>(emptyList())
    val members: State<List<HallOfFameMember>> = _members

    init {
        viewModelScope.launch {
            _members.value = repository.loadMembers()
        }
    }
}
