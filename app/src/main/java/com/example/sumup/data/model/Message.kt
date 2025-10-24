package com.example.sumup.data.model

data class Message(
    var messageId: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var content: String = "",
    var timestamp: Long = System.currentTimeMillis(),
    var isRead: Boolean = false
)
