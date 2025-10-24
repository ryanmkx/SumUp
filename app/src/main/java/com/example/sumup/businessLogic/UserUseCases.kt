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
    val level: String,
    val status: String = "active"
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
                if (userProfile.status.equals("disabled", ignoreCase = true)) {
                    return Result.failure(Exception("This account has been disabled. Please contact support."))
                }
                Result.success(
                    User(
                        userId = userProfile.userId,
                        username = userProfile.username,
                        email = userProfile.email,
                        profilePic = userProfile.profilePic,
                        level = userProfile.level,
                        status = userProfile.status
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
            val userId = authRepo.signUpWithEmail(email, password).getOrThrow()
            val userProfile = Users(
                userId = userId,
                username = username,
                email = email.trim().lowercase(),
                profilePic = "",
                level = "user",
                createdAt = Timestamp.now()
            )
            profileRepo.createUserProfile(userProfile)
            Result.success(
                User(
                    userId = userId,
                    username = username,
                    email = email,
                    profilePic = "",
                    level = "user",
                    status = "active"
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
    suspend fun execute(username: String, status: String = "active"): Result<User> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            val profileResult = profileRepo.getUserProfile(userId)
            if (profileResult.isSuccess) {
                val userProfile = profileResult.getOrNull()!!
                val updatedProfile = userProfile.copy(
                    userId = userProfile.userId,
                    username = username,
                    email = userProfile.email,
                    profilePic = userProfile.profilePic,
                    level = userProfile.level,
                    status = status,
                    friends = userProfile.friends,
                    friendsRequest = userProfile.friendsRequest,
                    createdAt = userProfile.createdAt
                )
                
                profileRepo.updateUserProfile(updatedProfile)
                
                Result.success(
                    User(
                        userId = userProfile.userId,
                        username = username,
                        email = userProfile.email,
                        profilePic = userProfile.profilePic,
                        level = userProfile.level,
                        status = status
                    )
                )
            } else {
                Result.failure(Exception("Failed to load user profile"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetFriendRequests(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(): Result<List<Users>> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            profileRepo.getFriendRequests(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class AcceptFriendRequest(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(friendUserId: String): Result<Unit> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            profileRepo.acceptFriendRequest(userId, friendUserId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class DeclineFriendRequest(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(friendUserId: String): Result<Unit> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            profileRepo.declineFriendRequest(userId, friendUserId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetUserProfile(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun execute(): Result<Users> {
        return try {
            val userId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            profileRepo.getUserProfile(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ChangePassword(
    private val authRepo: UserAuthRepository
) {
    suspend fun execute(currentPassword: String, newPassword: String): Result<Unit> {
        return try {
            println("DEBUG: ChangePassword - Attempting to change password")
            
            // Validate new password strength
            val validationResult = validatePasswordStrength(newPassword)
            if (validationResult.isNotEmpty()) {
                return Result.failure(Exception(validationResult))
            }
            
            // Change password using Firebase Auth
            val result = authRepo.changePassword(currentPassword, newPassword)
            if (result.isSuccess) {
                println("DEBUG: Password changed successfully")
            } else {
                println("DEBUG: Password change failed: ${result.exceptionOrNull()?.message}")
            }
            
            result
        } catch (e: Exception) {
            println("DEBUG: Exception in ChangePassword: ${e.message}")
            Result.failure(e)
        }
    }
    
    private fun validatePasswordStrength(password: String): String {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            !password.any { it.isDigit() } -> "Password must contain at least 1 digit"
            !password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) } -> "Password must contain at least 1 symbol"
            else -> ""
        }
    }
}

class ForgotPassword(
    private val authRepo: UserAuthRepository,
    private val profileRepo: UserProfileRepository
) {
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            println("DEBUG: ForgotPassword - Checking email: '$email'")
            
            // First check if email exists in our database
            val userResult = profileRepo.getUserProfileByEmail(email)
            if (userResult.isFailure) {
                val errorMsg = userResult.exceptionOrNull()?.message ?: "Unknown error"
                println("DEBUG: Email check failed: $errorMsg")
                return Result.failure(Exception("Email address not found in our system"))
            }
            
            println("DEBUG: Email found in database, sending reset email...")
            
            // If email exists, send password reset email
            val resetResult = authRepo.sendPasswordResetEmail(email)
            if (resetResult.isFailure) {
                println("DEBUG: Password reset email failed: ${resetResult.exceptionOrNull()?.message}")
            } else {
                println("DEBUG: Password reset email sent successfully")
            }
            
            resetResult
        } catch (e: Exception) {
            println("DEBUG: Exception in sendPasswordResetEmail: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun confirmPasswordReset(code: String, newPassword: String): Result<Unit> {
        return try {
            authRepo.confirmPasswordReset(code, newPassword)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}