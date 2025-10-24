package com.example.sumup.data.repository

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
    val status: String = "active", // User status: active, inactive, suspended, etc.
    val friends: List<String> = emptyList(), // Array of userIds who are friends
    val friendsRequest: List<String> = emptyList(), // Array of userIds who sent friend requests
    val createdAt: Timestamp? = null // It's good practice to make this nullable if it might not exist
)

// In the Data Layer
interface UserProfileRepository {
    suspend fun getUserProfile(userId: String): Result<Users>
    suspend fun getUserProfileByEmail(email: String): Result<Users>
    suspend fun createUserProfile(user: Users): Result<Unit>
    suspend fun updateUserProfile(user: Users): Result<Unit>
    suspend fun listUsersByLevel(level: String): Result<List<Users>>
    suspend fun searchUsers(query: String, level: String? = null): Result<List<Users>>
    suspend fun deleteUser(userId: String): Result<Unit>
    suspend fun disableUser(userId: String): Result<Unit>
    
    // Friend request management
    suspend fun sendFriendRequest(fromUserId: String, toUserId: String): Result<Unit>
    suspend fun acceptFriendRequest(userId: String, friendUserId: String): Result<Unit>
    suspend fun declineFriendRequest(userId: String, friendUserId: String): Result<Unit>
    suspend fun getFriendRequests(userId: String): Result<List<Users>>
    suspend fun getFriends(userId: String): Result<List<Users>>
    suspend fun addRejectionNotification(fromUserId: String, toUserId: String): Result<Unit>
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
            // Normalize email: trim whitespace and convert to lowercase
            val normalizedEmail = email.trim().lowercase()
            
            println("DEBUG: Searching for email: '$normalizedEmail'")
            
            val snapshot = db.collection("Users")
                .whereEqualTo("email", normalizedEmail)
                .limit(1)
                .get()
                .await()

            println("DEBUG: Query returned ${snapshot.documents.size} documents")
            
            val user = snapshot.documents.firstOrNull()?.toObject<Users>()
            if (user == null) {
                println("DEBUG: No user found for email: '$normalizedEmail'")
                return Result.failure(Exception("User profile not found for email"))
            }
            
            println("DEBUG: Found user: ${user.username} with email: ${user.email}")
            Result.success(user)
        } catch (e: Exception) {
            println("DEBUG: Error in getUserProfileByEmail: ${e.message}")
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

    override suspend fun listUsersByLevel(level: String): Result<List<Users>> {
        return try {
            val snapshot = db.collection("Users")
                .whereEqualTo("level", level)
                .get()
                .await()
            val users = snapshot.documents.mapNotNull { it.toObject<Users>() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchUsers(query: String, level: String?): Result<List<Users>> {
        // Reliable, index-free search: fetch users (optionally by level) and filter client-side.
        return try {
            val baseQuery = if (level != null) {
                db.collection("Users").whereEqualTo("level", level)
            } else {
                db.collection("Users")
            }
            val snapshot = baseQuery.get().await()
            val lower = query.trim().lowercase()
            val users = snapshot.documents
                .mapNotNull { it.toObject<Users>() }
                .filter { u ->
                    if (lower.isEmpty()) true else {
                        u.username.lowercase().contains(lower) || u.email.lowercase().contains(lower)
                    }
                }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            db.collection("Users").document(userId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun disableUser(userId: String): Result<Unit> {
        return try {
            db.collection("Users").document(userId)
                .update("status", "disabled")
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendFriendRequest(fromUserId: String, toUserId: String): Result<Unit> {
        return try {
            // Add fromUserId to toUser's friendsRequest array (receiver gets the request)
            val result = db.collection("Users").document(toUserId)
                .update("friendsRequest", com.google.firebase.firestore.FieldValue.arrayUnion(fromUserId))
                .await()
            
            // Debug: Check if the update was successful
            val updatedDoc = db.collection("Users").document(toUserId).get().await()
            val updatedUser = updatedDoc.toObject<Users>()
            println("DEBUG: Friend request sent. Updated user's friendsRequest: ${updatedUser?.friendsRequest}")
            
            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG: Error sending friend request: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun acceptFriendRequest(userId: String, friendUserId: String): Result<Unit> {
        // Safety: never add self as friend even if bad data exists
        if (userId == friendUserId) {
            return Result.failure(IllegalArgumentException("Cannot add self as friend; friendUserId == userId"))
        }
        return try {
            val batch = db.batch()
            
            // Add friendUserId to user's friends array
            val userRef = db.collection("Users").document(userId)
            batch.update(userRef, "friends", com.google.firebase.firestore.FieldValue.arrayUnion(friendUserId))
            
            // Remove friendUserId from user's friendsRequest array
            batch.update(userRef, "friendsRequest", com.google.firebase.firestore.FieldValue.arrayRemove(friendUserId))
            
            // Add userId to friend's friends array (mutual friendship)
            val friendRef = db.collection("Users").document(friendUserId)
            batch.update(friendRef, "friends", com.google.firebase.firestore.FieldValue.arrayUnion(userId))
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun declineFriendRequest(userId: String, friendUserId: String): Result<Unit> {
        return try {
            val batch = db.batch()
            
            // Remove friendUserId from user's friendsRequest array
            val userRef = db.collection("Users").document(userId)
            batch.update(userRef, "friendsRequest", com.google.firebase.firestore.FieldValue.arrayRemove(friendUserId))
            
            // Add rejection notification to the sender's document
            val rejectionData = mapOf(
                "type" to "friend_request_rejected",
                "fromUserId" to userId,
                "toUserId" to friendUserId,
                "timestamp" to Timestamp.now()
            )
            batch.set(
                db.collection("Notifications").document("${friendUserId}_${userId}_rejected"),
                rejectionData
            )
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addRejectionNotification(fromUserId: String, toUserId: String): Result<Unit> {
        return try {
            val rejectionData = mapOf(
                "type" to "friend_request_rejected",
                "fromUserId" to fromUserId,
                "toUserId" to toUserId,
                "timestamp" to Timestamp.now()
            )
            
            db.collection("Notifications").document("${toUserId}_${fromUserId}_rejected")
                .set(rejectionData)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getFriendRequests(userId: String): Result<List<Users>> {
        return try {
            val userDoc = db.collection("Users").document(userId).get().await()
            val user = userDoc.toObject<Users>() ?: return Result.failure(Exception("User not found"))
            
            println("DEBUG: Getting friend requests for user: $userId")
            println("DEBUG: User's friendsRequest array: ${user.friendsRequest}")
            
            if (user.friendsRequest.isEmpty()) {
                println("DEBUG: No friend requests found")
                return Result.success(emptyList())
            }
            
            // Get user documents by their document IDs (which are the userIds in friendsRequest array)
            val friendRequests = mutableListOf<Users>()
            for (friendUserId in user.friendsRequest) {
                try {
                    val friendDoc = db.collection("Users").document(friendUserId).get().await()
                    val friend = friendDoc.toObject<Users>()
                    if (friend != null) {
                        friendRequests.add(friend)
                        println("DEBUG: Found friend request from: ${friend.username}")
                    }
                } catch (e: Exception) {
                    println("DEBUG: Error getting friend $friendUserId: ${e.message}")
                    // Skip this user if there's an error
                    continue
                }
            }
            
            println("DEBUG: Total friend requests found: ${friendRequests.size}")
            Result.success(friendRequests)
        } catch (e: Exception) {
            println("DEBUG: Error in getFriendRequests: ${e.message}")
            Result.failure(e)
        }
    }
    
    override suspend fun getFriends(userId: String): Result<List<Users>> {
        return try {
            val userDoc = db.collection("Users").document(userId).get().await()
            val user = userDoc.toObject<Users>() ?: return Result.failure(Exception("User not found"))
            
            if (user.friends.isEmpty()) {
                return Result.success(emptyList())
            }
            
            // Get user documents by their document IDs (which are the userIds in friends array)
            val friends = mutableListOf<Users>()
            for (friendUserId in user.friends) {
                try {
                    val friendDoc = db.collection("Users").document(friendUserId).get().await()
                    val friend = friendDoc.toObject<Users>()
                    if (friend != null) {
                        friends.add(friend)
                    }
                } catch (e: Exception) {
                    // Skip this user if there's an error
                    continue
                }
            }
            
            Result.success(friends)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}