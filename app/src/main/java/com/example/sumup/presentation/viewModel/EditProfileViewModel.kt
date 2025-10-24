package com.example.sumup.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.businessLogic.DependencyModule
import com.example.sumup.data.repository.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EditProfileViewModel(
    private val userRepository: com.example.sumup.data.repository.UserProfileRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) : ViewModel() {

    private val _userProfile = MutableStateFlow<Users?>(null)
    val userProfile: StateFlow<Users?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isUploadingImage = MutableStateFlow(false)
    val isUploadingImage: StateFlow<Boolean> = _isUploadingImage.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val result = userRepository.getUserProfile(currentUser.uid)
                    result.fold(
                        onSuccess = { user ->
                            _userProfile.value = user
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "Failed to load user profile"
                        }
                    )
                } else {
                    _error.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateProfile(username: String, status: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val currentProfile = _userProfile.value
                    if (currentProfile != null) {
                        val updatedUser = currentProfile.copy(
                            username = username,
                            status = status
                        )
                        
                        val result = userRepository.updateUserProfile(updatedUser)
                        result.fold(
                            onSuccess = {
                                _userProfile.value = updatedUser
                            },
                            onFailure = { exception ->
                                _error.value = exception.message ?: "Failed to update profile"
                            }
                        )
                    } else {
                        _error.value = "User profile not loaded"
                    }
                } else {
                    _error.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadProfileImage(imageUri: Uri) {
        viewModelScope.launch {
            _isUploadingImage.value = true
            _error.value = null
            
            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    // Generate unique filename
                    val fileName = "profile_images/${currentUser.uid}/${UUID.randomUUID()}.jpg"
                    
                    // Upload to Firebase Storage
                    val storageRef = firebaseStorage.reference.child(fileName)
                    val uploadTask = storageRef.putFile(imageUri).await()
                    
                    // Get download URL
                    val downloadUrl = storageRef.downloadUrl.await()
                    
                    // Update user profile with new image URL
                    val currentProfile = _userProfile.value
                    if (currentProfile != null) {
                        val updatedUser = currentProfile.copy(
                            profilePic = downloadUrl.toString()
                        )
                        
                        val result = userRepository.updateUserProfile(updatedUser)
                        result.fold(
                            onSuccess = {
                                _userProfile.value = updatedUser
                            },
                            onFailure = { exception ->
                                _error.value = exception.message ?: "Failed to update profile with new image"
                            }
                        )
                    } else {
                        _error.value = "User profile not loaded"
                    }
                } else {
                    _error.value = "User not authenticated"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to upload image"
            } finally {
                _isUploadingImage.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
