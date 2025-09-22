package com.example.sumup.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

// This is the data class that represents a user profile in your Firestore database.
// It was missing from the provided code snippet, causing "Unresolved reference: Users".
data class Users(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val profilePic: String = "",
    val level: String = "",
    val createdAt: Timestamp? = null // It's good practice to make this nullable if it might not exist
)

// In the Data Layer
interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): Result<Users>
    suspend fun getUserProfileByEmail(email: String): Result<Users>
    suspend fun createUserProfile(user: Users): Result<Unit>
    suspend fun updateUserProfile(user: Users): Result<Unit>
}

class FirestoreUserProfileRepository(private val db: FirebaseFirestore) : UserProfileRepository {
    override suspend fun getUserProfile(userId: String): Result<Users> {
        return try {
            // Unresolved reference: `await()` is part of the kotlinx-coroutines-play-services library
            // You need to add this dependency and import kotlinx.coroutines.tasks.await
            val snapshot = db.collection("Users").document(userId).get().await()

            // `toObject<Users>()` requires the import com.google.firebase.firestore.ktx.toObject
            val user = snapshot.toObject<Users>()
                ?: return Result.failure(Exception("User profile not found"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserProfileByEmail(email: String): Result<Users> {
        return try {
            val snapshot = db.collection("Users")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()

            val user = snapshot.documents.firstOrNull()?.toObject<Users>()
                ?: return Result.failure(Exception("User profile not found for email"))

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserProfile(user: Users): Result<Unit> {
        return try {
            db.collection("Users").document(user.userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(user: Users): Result<Unit> {
        return try {
            db.collection("Users").document(user.userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}