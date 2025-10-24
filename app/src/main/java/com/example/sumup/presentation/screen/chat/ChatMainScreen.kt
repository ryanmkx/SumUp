package com.example.sumup.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import com.example.sumup.presentation.screen.common.Header
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun ChatMainScreen(
    userName: String = "User", // current logged-in user's name
    onChatClick: (String) -> Unit = {}, // callback when user clicks on chat
    onFooterNavigate: (FooterNavigation) -> Unit = {},
    onAddFriendClick: () -> Unit = {}, // callback when user clicks add friend button
    onNotificationClick: () -> Unit = {}, // callback when user clicks notification icon
    friends: List<ChatFriend> = emptyList(), // list of friends to display
    unreadCounts: Map<String, Int> = emptyMap() // unread message counts per friend
) {
    Scaffold(
        topBar = {
            Header(
                title = "Chat"
            )
        },
        bottomBar = {
            FooterBar(
                currentRoute = FooterNavigation.Chat,
                onNavigate = onFooterNavigate
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddFriendClick,
                containerColor = purpleMain,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Friend"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Greeting
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello,",
                        fontSize = 20.sp,
                        color = purpleMain
                    )
                    Text(
                        text = userName,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                // Notification icon
                IconButton(
                    onClick = onNotificationClick,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(purpleMain.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Friend Requests",
                        tint = purpleMain,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Chat list
            if (friends.isNotEmpty()) {
                friends.forEach { friend ->
                    val unreadCount = unreadCounts[friend.userId] ?: 0
                    ChatItem(
                        imageRes = friend.imageRes,
                        name = friend.name,
                        hasMessage = unreadCount > 0,
                        unreadCount = unreadCount,
                        profilePicUrl = friend.profilePicUrl,
                        onClick = { onChatClick(friend.name) }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                }
            } else {
                // Default chat items when no friends are added yet
                Text(
                    text = "Add New Friends to Have Chat!",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ChatItem(
    imageRes: Int,
    name: String,
    hasMessage: Boolean = false,
    unreadCount: Int = 0,
    profilePicUrl: String = "", // Add profile picture URL parameter
    onClick: () -> Unit = {} // new parameter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(80.dp)
            .clickable { onClick() }, // clickable card
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White // force card background to white
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            if (profilePicUrl.isNotEmpty()) {
                AsyncImage(
                    model = profilePicUrl,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Name only
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            // Unread indicator (dot only, no number)
            if (hasMessage) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(purpleMain)
                )
            }
        }
    }
}

data class ChatFriend(
    val name: String,
    val hasUnreadMessage: Boolean = false,
    val imageRes: Int = R.drawable.logo,
    val profilePicUrl: String = "", // Add profile picture URL from Firebase Storage
    val userId: String = "" // Add userId for navigation
)

@Preview(showBackground = true)
@Composable
fun ChatMainScreenPreview() {
    ChatMainScreen(
        userName = "John Doe", // Preview with sample user name
        onChatClick = { name ->
            println("Clicked on chat: $name")
        },
        onAddFriendClick = {
            println("Add friend clicked")
        },
        onNotificationClick = {
            println("Notification clicked")
        },
        friends = listOf(
            ChatFriend(
                name = "John Doe",
                hasUnreadMessage = true
            ),
            ChatFriend(
                name = "Jane Smith",
                hasUnreadMessage = false
            )
        )
    )
}
