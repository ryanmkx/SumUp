package com.example.sumup.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sumup.businessLogic.*
import com.example.sumup.data.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val sendMessageUseCase: SendMessage,
    private val getMessagesUseCase: GetMessages,
    private val createChatRoomUseCase: CreateChatRoom,
    private val markMessageAsReadUseCase: MarkMessageAsRead,
    private val getUnreadMessageCountUseCase: GetUnreadMessageCount
) : ViewModel() {
    
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _unreadCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val unreadCounts: StateFlow<Map<String, Int>> = _unreadCounts.asStateFlow()
    
    fun sendMessage(message: Message) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            sendMessageUseCase.execute(message)
                .onSuccess {
                    // Message will be updated through the flow
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to send message"
                }
            
            _isLoading.value = false
        }
    }
    
    fun getMessages(chatRoomId: String) {
        viewModelScope.launch {
            getMessagesUseCase.execute(chatRoomId).collect { messagesList ->
                _messages.value = messagesList
            }
        }
    }
    
    fun createChatRoom(friendId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            createChatRoomUseCase.execute(friendId)
                .onFailure { exception ->
                    _error.value = exception.message ?: "Failed to create chat room"
                }
            
            _isLoading.value = false
        }
    }
    
    fun markMessageAsRead(messageId: String, chatRoomId: String) {
        viewModelScope.launch {
            markMessageAsReadUseCase.execute(messageId, chatRoomId)
                .onFailure { exception ->
                    // Don't show error for read status updates
                    println("Failed to mark message as read: ${exception.message}")
                }
        }
    }
    
    fun getUnreadMessageCount() {
        viewModelScope.launch {
            getUnreadMessageCountUseCase.execute().collect { counts ->
                _unreadCounts.value = counts
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}
