package com.example.reminderapp.presentation.edit_lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Group
import com.example.domain.use_case.group.GetAllGroupsUseCase
import com.example.domain.use_case.reminder.DeleteReminderGroupUseCase
import com.example.reminderapp.presentation.base.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EditListsViewModel(
    private val getAllGroupsUseCase: GetAllGroupsUseCase,
    private val deleteReminderGroupUseCase: DeleteReminderGroupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<Group>>>(UiState.Loading)
    val uiState get() = _uiState

    fun fetchData() {
        viewModelScope.launch {
            getAllGroupsUseCase()
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    fun deleteGroup(groupId: Int) {
        viewModelScope.launch {
            /** use case execute method here */
            try {
                deleteReminderGroupUseCase(groupId)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.toString())
            }
        }
    }

}