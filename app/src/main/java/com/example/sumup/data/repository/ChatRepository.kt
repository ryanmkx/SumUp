package com.example.sumup.data.repository

import com.example.sumup.data.model.Message
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*

interface ChatRepository {
    suspend fun sendMessage(message: Message): Result<Unit>
    fun getMessages(chatRoomId: String): Flow<List<Message>>
    suspend fun createChatRoom(userId1: String, userId2: String): Result<String>
    suspend fun markMessageAsRead(messageId: String, chatRoomId: String): Result<Unit>
    fun getUnreadMessageCount(userId: String): Flow<Map<String, Int>>
}

class FirebaseChatRepository(private val database: FirebaseDatabase) : ChatRepository {
    
    override suspend fun sendMessage(message: Message): Result<Unit> {
        return try {
            println("DEBUG: ChatRepository - Starting sendMessage")
            println("DEBUG: Database reference: ${database.reference}")
            
            // Test connectivity first
            println("DEBUG: Testing Firebase connectivity...")
            val testRef = database.reference.child("test").child("connectivity")
            testRef.setValue("test").await()
            println("DEBUG: Firebase connectivity test successful")
            
            val messageId = database.reference.child("messages").push().key
                ?: return Result.failure(Exception("Failed to generate message ID"))
            
            println("DEBUG: Generated message ID: $messageId")
            
            message.messageId = messageId
            message.timestamp = System.currentTimeMillis()
            
            println("DEBUG: Message data: $message")
            
            // Save message to messages collection
            println("DEBUG: Saving message to Firebase...")
            database.reference.child("messages").child(messageId).setValue(message).await()
            println("DEBUG: Message saved successfully")
            
            // Update chat room with latest message info
            val chatRoomId = getChatRoomId(message.senderId, message.receiverId)
            println("DEBUG: Chat room ID: $chatRoomId")
            
            database.reference.child("chatRooms").child(chatRoomId).child("lastMessage").setValue(message.content).await()
            database.reference.child("chatRooms").child(chatRoomId).child("lastMessageTime").setValue(message.timestamp).await()
            println("DEBUG: Chat room updated successfully")
            
            Result.success(Unit)
        } catch (e: Exception) {
            println("DEBUG: Error in sendMessage: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }
    
    override fun getMessages(chatRoomId: String): Flow<List<Message>> = callbackFlow {
        val messagesRef = database.reference.child("messages")
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = mutableListOf<Message>()
                
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null && 
                        (message.senderId == chatRoomId.split("_")[0] || message.senderId == chatRoomId.split("_")[1]) &&
                        (message.receiverId == chatRoomId.split("_")[0] || message.receiverId == chatRoomId.split("_")[1])) {
                        messages.add(message)
                    }
                }
                
                // Sort messages by timestamp
                messages.sortBy { it.timestamp }
                trySend(messages)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        messagesRef.addValueEventListener(listener)
        
        awaitClose {
            messagesRef.removeEventListener(listener)
        }
    }
    
    override suspend fun createChatRoom(userId1: String, userId2: String): Result<String> {
        return try {
            val chatRoomId = getChatRoomId(userId1, userId2)
            
            val chatRoomData = mapOf(
                "participants" to listOf(userId1, userId2),
                "createdAt" to System.currentTimeMillis(),
                "lastMessage" to "",
                "lastMessageTime" to 0L
            )
            
            database.reference.child("chatRooms").child(chatRoomId).setValue(chatRoomData).await()
            Result.success(chatRoomId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun markMessageAsRead(messageId: String, chatRoomId: String): Result<Unit> {
        return try {
            val msgRef = database.reference.child("messages").child(messageId)
            // Set both new and legacy flags to cover older records
            msgRef.child("isRead").setValue(true).await()
            msgRef.child("read").setValue(true).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getUnreadMessageCount(userId: String): Flow<Map<String, Int>> = callbackFlow {
        val messagesRef = database.reference
            .child("messages")
            .orderByChild("receiverId")
            .equalTo(userId)
        
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val unreadCounts = mutableMapOf<String, Int>()
                
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null) {
                        val legacyRead = messageSnapshot.child("read").getValue(Boolean::class.java) ?: false
                        val isUnread = !message.isRead && !legacyRead
                        if (isUnread) {
                            val senderId = message.senderId
                            unreadCounts[senderId] = (unreadCounts[senderId] ?: 0) + 1
                        }
                    }
                }
                
                trySend(unreadCounts)
            }
            
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        
        (messagesRef as Query).addValueEventListener(listener)
        
        awaitClose {
            (messagesRef as Query).removeEventListener(listener)
        }
    }
    
    private fun getChatRoomId(userId1: String, userId2: String): String {
        // Create consistent chat room ID by sorting user IDs
        val sortedIds = listOf(userId1, userId2).sorted()
        return "${sortedIds[0]}_${sortedIds[1]}"
    }
}
