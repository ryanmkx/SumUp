package com.example.sumup.data

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface UserAuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<String>
    suspend fun signUpWithEmail(email: String, password: String): Result<String>
    fun signOut()
    fun getCurrentUserId(): String?
}

class FirebaseAuthRepository(private val auth: FirebaseAuth) : UserAuthRepository {
    override suspend fun signInWithEmail(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return Result.failure(Exception("Login failed"))
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUpWithEmail(email: String, password: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return Result.failure(Exception("Sign up failed"))
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}