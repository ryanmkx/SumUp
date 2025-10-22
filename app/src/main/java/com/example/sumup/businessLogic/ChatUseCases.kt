package com.example.sumup.businessLogic

import com.example.sumup.data.model.Message
import com.example.sumup.data.repository.ChatRepository
import com.example.sumup.data.repository.UserAuthRepository
import kotlinx.coroutines.flow.Flow

// Use cases for chat operations following business logic layer principles
class SendMessage(
    private val authRepo: UserAuthRepository,
    private val chatRepo: ChatRepository
) {
    suspend fun execute(message: Message): Result<Unit> {
        return try {
            val currentUserId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            // Validate message
            if (message.content.isBlank()) {
                return Result.failure(Exception("Message cannot be empty"))
            }
            
            if (message.senderId != currentUserId) {
                return Result.failure(Exception("Invalid sender"))
            }
            
            chatRepo.sendMessage(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetMessages(
    private val authRepo: UserAuthRepository,
    private val chatRepo: ChatRepository
) {
    fun execute(chatRoomId: String): Flow<List<Message>> {
        val currentUserId = authRepo.getCurrentUserId()
        if (currentUserId == null) {
            return kotlinx.coroutines.flow.flowOf(emptyList())
        }
        
        return chatRepo.getMessages(chatRoomId)
    }
}

class CreateChatRoom(
    private val authRepo: UserAuthRepository,
    private val chatRepo: ChatRepository
) {
    suspend fun execute(friendId: String): Result<String> {
        return try {
            val currentUserId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            if (friendId.isBlank()) {
                return Result.failure(Exception("Friend ID cannot be empty"))
            }
            
            chatRepo.createChatRoom(currentUserId, friendId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class MarkMessageAsRead(
    private val authRepo: UserAuthRepository,
    private val chatRepo: ChatRepository
) {
    suspend fun execute(messageId: String, chatRoomId: String): Result<Unit> {
        return try {
            val currentUserId = authRepo.getCurrentUserId()
                ?: return Result.failure(Exception("User not authenticated"))
            
            chatRepo.markMessageAsRead(messageId, chatRoomId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class GetUnreadMessageCount(
    private val authRepo: UserAuthRepository,
    private val chatRepo: ChatRepository
) {
    fun execute(): Flow<Map<String, Int>> {
        val currentUserId = authRepo.getCurrentUserId()
        if (currentUserId == null) {
            return kotlinx.coroutines.flow.flowOf(emptyMap())
        }
        
        return chatRepo.getUnreadMessageCount(currentUserId)
    }
}
