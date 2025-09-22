package com.example.sumup.businessLogic

import com.example.sumup.data.repository.UserAuthRepository
import com.example.sumup.data.repository.UserProfileRepository
import com.example.sumup.data.repository.Users
import com.google.firebase.Timestamp

// Domain model for User (not tied to Firebase)
data class User(
    val userId: String,
    val username: String,
    val email: String,
    val profilePic: String,
    val level: String
)

class LogIn(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(email: String, password: String): Result<User> {
        return try {
            val userId = authRepo.signInWithEmail(email, password).getOrThrow()
            val profileResult = profileRepo.getUserProfile(userId)
            if (profileResult.isSuccess) {
                val userProfile = profileResult.getOrNull()!!
                Result.success(
                    User(
                        userId = userProfile.userId,
                        username = userProfile.username,
                        email = userProfile.email,
                        profilePic = userProfile.profilePic,
                        level = userProfile.level
                    )
                )
            } else {
                Result.success(
                    User(
                        userId = userId,
                        username = "",
                        email = email,
                        profilePic = "",
                        level = ""
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class SignUp(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(email: String, username: String, password: String): Result<User> {
        return try {
            // Create user account with Firebase Auth
            val userId = authRepo.signUpWithEmail(email, password).getOrThrow()
            
            // Create user profile in Firestore
            val userProfile = Users(
                userId = userId,
                username = username,
                email = email,
                profilePic = "",
                level = "user",
                createdAt = Timestamp.now()
            )
            
            profileRepo.createUserProfile(userProfile).getOrThrow()
            
            Result.success(
                User(
                    userId = userId,
                    username = username,
                    email = email,
                    profilePic = "",
                    level = "user"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class UpdateProfile(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(username: String): Result<User> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            // Get current user profile to preserve existing data
            val currentProfile = profileRepo.getUserProfile(userId).getOrThrow()
            
            // Update the profile with new username
            val updatedProfile = currentProfile.copy(
                username = username
            )
            
            profileRepo.updateUserProfile(updatedProfile).getOrThrow()
            
            Result.success(
                User(
                    userId = userId,
                    username = username,
                    email = currentProfile.email,
                    profilePic = currentProfile.profilePic,
                    level = currentProfile.level
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
