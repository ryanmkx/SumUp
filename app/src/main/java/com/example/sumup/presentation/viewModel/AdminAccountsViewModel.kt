package com.example.sumup.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.businessLogic.DeleteUserAccount
import com.example.sumup.businessLogic.ListUsersByLevel
import com.example.sumup.businessLogic.SearchUsers
import com.example.sumup.data.repository.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminAccountsViewModel(
    private val listUsersByLevel: ListUsersByLevel,
    private val searchUsers: SearchUsers,
    private val deleteUserAccount: DeleteUserAccount
) : ViewModel() {

    private val _users = MutableStateFlow<List<Users>>(emptyList())
    val users: StateFlow<List<Users>> = _users.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            listUsersByLevel.execute("user")
                .onSuccess { list -> _users.value = list.filter { it.status.lowercase() != "disabled" } }
                .onFailure { _error.value = it.message ?: "Failed to load users" }
            _isLoading.value = false
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            loadUsers()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            searchUsers.execute(query, level = "user")
                .onSuccess { list -> _users.value = list.filter { it.status.lowercase() != "disabled" } }
                .onFailure { _error.value = it.message ?: "Search failed" }
            _isLoading.value = false
        }
    }

    fun deleteUser(userId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            deleteUserAccount.execute(userId)
                .onSuccess {
                    onSuccess()
                    loadUsers()
                }
                .onFailure { e ->
                    val msg = e.message ?: "Delete failed"
                    _error.value = msg
                    onError(msg)
                }
            _isLoading.value = false
        }
    }
}


