package com.example.sumup.presentation.screen.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.data.repository.FirestoreUserProfileRepository
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFriendScreen(
    onBack: () -> Unit = {},
    onAddFriend: (String) -> Unit = {} // callback when friend is added
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<UserSearchResult>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userRepository = remember { FirestoreUserProfileRepository(FirebaseFirestore.getInstance()) }
    
    // Get current authenticated user ID
    val currentUserId by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid) }

    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Add Friends"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Search section
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Search by Email",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = purpleMain,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Search for users to add as friends",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Enter email address") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Button(
                        onClick = {
                            if (searchQuery.isNotBlank()) {
                                isLoading = true
                                searchResults = emptyList() // Clear previous results
                                
                                coroutineScope.launch {
                                    try {
                                        val result = userRepository.getUserProfileByEmail(searchQuery.trim())
                                        if (result.isSuccess) {
                                            val user = result.getOrNull()!!
                                            
                                            // Check if user level is "user" and status is "active"
                                            if (user.level.lowercase() == "user" && user.status.lowercase() == "active") {
                                                searchResults = listOf(
                                                    UserSearchResult(
                                                        email = user.email,
                                                        username = user.username,
                                                        userId = user.userId,
                                                        isAlreadyFriend = false // TODO: Check if already friends
                                                    )
                                                )
                                            } else {
                                                // User found but not eligible (not regular user or not active)
                                                searchResults = emptyList()
                                                snackbarMessage = when {
                                                    user.level.lowercase() != "user" -> {
                                                        "This user cannot be added as a friend"
                                                    }

                                                    user.status.lowercase() != "active" -> {
                                                        "This user account is not active"
                                                    }

                                                    else -> {
                                                        "This user cannot be added as a friend"
                                                    }
                                                }
                                                showSnackbar = true
                                            }
                                        } else {
                                            // User not found
                                            searchResults = emptyList()
                                            snackbarMessage = "No user found with email: $searchQuery"
                                            showSnackbar = true
                                        }
                                    } catch (e: Exception) {
                                        searchResults = emptyList()
                                        snackbarMessage = "Error searching for user: ${e.message}"
                                        showSnackbar = true
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = searchQuery.isNotBlank() && !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = purpleMain)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Search")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Search results
            if (searchResults.isNotEmpty()) {
                Text(
                    text = "Search Results",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { result ->
                        UserSearchResultItem(
                            user = result,
                            onAddClick = {
                                coroutineScope.launch {
                                    try {
                                        val senderId = currentUserId
                                        if (senderId == null) {
                                            snackbarMessage = "You must be logged in to send friend requests"
                                            showSnackbar = true
                                            return@launch
                                        }
                                        val sendResult = userRepository.sendFriendRequest(senderId, result.userId)
                                        if (sendResult.isSuccess) {
                                            snackbarMessage = "Friend request sent to ${result.username}!"
                                            showSnackbar = true
                                            onAddFriend(result.userId)
                                        } else {
                                            snackbarMessage = "Failed to send friend request: ${sendResult.exceptionOrNull()?.message}"
                                            showSnackbar = true
                                        }
                                    } catch (e: Exception) {
                                        snackbarMessage = "Error sending friend request: ${e.message}"
                                        showSnackbar = true
                                    }
                                }
                            }
                        )
                    }
                }
            } else if (searchQuery.isNotBlank() && !isLoading) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No eligible users found with that email",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
    
    // Snackbar for feedback
    if (showSnackbar) {
        LaunchedEffect(showSnackbar) {
            kotlinx.coroutines.delay(3000)
            showSnackbar = false
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier.padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.8f))
            ) {
                Text(
                    text = snackbarMessage,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun UserSearchResultItem(
    user: UserSearchResult,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            if (user.isAlreadyFriend) {
                Text(
                    text = "Already Friends",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(end = 8.dp)
                )
            } else {
                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(containerColor = purpleMain)
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Add Friend",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            }
        }
    }
}

data class UserSearchResult(
    val email: String,
    val username: String,
    val userId: String,
    val isAlreadyFriend: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun AddFriendScreenPreview() {
    AddFriendScreen(
        onBack = { },
        onAddFriend = { userId -> println("Adding friend with ID: $userId") }
    )
}
