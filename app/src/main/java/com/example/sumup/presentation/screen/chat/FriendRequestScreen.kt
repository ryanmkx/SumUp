package com.example.sumup.presentation.screen.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sumup.data.repository.Users
import com.example.sumup.businessLogic.DependencyModule
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain
import com.example.sumup.presentation.viewModel.FriendRequestViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestScreen(
    onBack: () -> Unit = {},
    onAcceptRequest: (String) -> Unit = {}, // callback when user accepts friend request
    onDeclineRequest: (String) -> Unit = {} // callback when user declines friend request
) {
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    // Use ViewModel instead of direct repository access - follows 3-layer architecture
    val viewModel: FriendRequestViewModel = viewModel { DependencyModule.createFriendRequestViewModel() }
    
    // Collect state from ViewModel
    val friendRequests by viewModel.friendRequests.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    
    // Load friend requests when screen loads
    LaunchedEffect(Unit) {
        viewModel.loadFriendRequests()
    }
    
    // Handle error messages
    LaunchedEffect(error) {
        error?.let { errorMsg ->
            snackbarMessage = errorMsg
            showSnackbar = true
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Friend Requests"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header text
//            Text(
//                text = "Friend Requests",
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = purpleMain,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Friend requests list
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = purpleMain)
                }
            } else if (friendRequests.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(friendRequests) { request ->
                        FriendRequestItem(
                            request = request,
                            onAccept = {
                                // Use ViewModel to accept friend request - follows 3-layer architecture
                                viewModel.acceptFriendRequest(request.userId)
                                snackbarMessage = "Friend request accepted!"
                                showSnackbar = true
                                onAcceptRequest(request.userId)
                            },
                            onDecline = {
                                // Use ViewModel to decline friend request - follows 3-layer architecture
                                viewModel.declineFriendRequest(request.userId)
                                snackbarMessage = "Friend request declined"
                                showSnackbar = true
                                onDeclineRequest(request.userId)
                            }
                        )
                    }
                }
            } else {
                // No requests message
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No friend requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "You're all caught up!",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
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
}

@Composable
fun FriendRequestItem(
    request: Users,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile placeholder (you can add actual profile image here)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(purpleMain.copy(alpha = 0.1f), RoundedCornerShape(25.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = request.username.take(1).uppercase(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = purpleMain
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // User info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = request.username,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = request.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Accept button
                IconButton(
                    onClick = onAccept,
                    modifier = Modifier
                        .size(40.dp)
                        .background(purpleMain, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                // Decline button
                IconButton(
                    onClick = onDecline,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Decline",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FriendRequestScreenPreview() {
    FriendRequestScreen(
        onBack = { },
        onAcceptRequest = { userId -> println("Accepted request from: $userId") },
        onDeclineRequest = { userId -> println("Declined request from: $userId") }
    )
}
