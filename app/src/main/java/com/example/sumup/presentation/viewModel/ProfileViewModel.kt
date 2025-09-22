package com.example.sumup.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.businessLogic.LogIn
import com.example.sumup.businessLogic.SignUp
import com.example.sumup.businessLogic.UpdateProfile
import com.example.sumup.businessLogic.User
import com.example.sumup.data.repository.UserAuthRepository
import com.example.sumup.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) : ViewModel() {
    
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()
    
    private val logInUseCase = LogIn(authRepo, profileRepo)
    private val signUpUseCase = SignUp(authRepo, profileRepo)
    private val updateProfileUseCase = UpdateProfile(authRepo, profileRepo)
    
    init {
        checkCurrentUser()
    }
    
    private fun checkCurrentUser() {
        viewModelScope.launch {
            val userId = authRepo.getCurrentUserId()
            if (userId != null) {
                _isLoggedIn.value = true
                loadUserProfile()
            } else {
                _isLoggedIn.value = false
            }
        }
    }
    
    private fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val userId = authRepo.getCurrentUserId()
            if (userId != null) {
                profileRepo.getUserProfile(userId)
                    .onSuccess { userProfile ->
                        _user.value = User(
                            userId = userProfile.userId,
                            username = userProfile.username,
                            email = userProfile.email,
                            profilePic = userProfile.profilePic,
                            level = userProfile.level
                        )
                    }
                    .onFailure { exception ->
                        _error.value = exception.message ?: "Failed to load user profile"
                    }
            }
            
            _isLoading.value = false
        }
    }
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            logInUseCase.execute(email, password)
                .onSuccess { user ->
                    _user.value = user
                    _isLoggedIn.value = true
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Login failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun signUp(email: String, username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            signUpUseCase.execute(email, username, password)
                .onSuccess { user ->
                    _user.value = user
                    _isLoggedIn.value = true
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Sign up failed"
                }
            
            _isLoading.value = false
        }
    }
    
    fun updateProfile(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            updateProfileUseCase.execute(username)
                .onSuccess { user ->
                    _user.value = user
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to update profile"
                }
            
            _isLoading.value = false
        }
    }
    
    fun signOut() {
        authRepo.signOut()
        _user.value = null
        _isLoggedIn.value = false
        _error.value = null
    }
    
    fun clearError() {
        _error.value = null
    }
}