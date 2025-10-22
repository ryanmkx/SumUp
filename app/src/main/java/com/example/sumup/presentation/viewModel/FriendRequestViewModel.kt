package com.example.sumup.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.businessLogic.*
import com.example.sumup.data.repository.Users
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FriendRequestViewModel(
    private val getFriendRequestsUseCase: GetFriendRequests,
    private val acceptFriendRequestUseCase: AcceptFriendRequest,
    private val declineFriendRequestUseCase: DeclineFriendRequest
) : ViewModel() {
    
    private val _friendRequests = MutableStateFlow<List<Users>>(emptyList())
    val friendRequests: StateFlow<List<Users>> = _friendRequests.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadFriendRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            getFriendRequestsUseCase.execute()
                .onSuccess { requests ->
                    _friendRequests.value = requests
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load friend requests"
                }
            
            _isLoading.value = false
        }
    }
    
    fun acceptFriendRequest(friendUserId: String) {
        viewModelScope.launch {
            acceptFriendRequestUseCase.execute(friendUserId)
                .onSuccess {
                    // Remove from local list
                    _friendRequests.value = _friendRequests.value.filter { it.userId != friendUserId }
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to accept friend request"
                }
        }
    }
    
    fun declineFriendRequest(friendUserId: String) {
        viewModelScope.launch {
            declineFriendRequestUseCase.execute(friendUserId)
                .onSuccess {
                    // Remove from local list
                    _friendRequests.value = _friendRequests.value.filter { it.userId != friendUserId }
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to decline friend request"
                }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}

class EditProfileViewModel(
    private val getUserProfileUseCase: GetUserProfile,
    private val updateProfileUseCase: UpdateProfile
) : ViewModel() {
    
    private val _userProfile = MutableStateFlow<Users?>(null)
    val userProfile: StateFlow<Users?> = _userProfile.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            getUserProfileUseCase.execute()
                .onSuccess { profile ->
                    _userProfile.value = profile
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to load user profile"
                }
            
            _isLoading.value = false
        }
    }
    
    fun updateProfile(username: String, status: String = "active") {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            updateProfileUseCase.execute(username, status)
                .onSuccess { user ->
                    // Update local profile
                    _userProfile.value = _userProfile.value?.copy(username = username, status = status)
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to update profile"
                }
            
            _isLoading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
