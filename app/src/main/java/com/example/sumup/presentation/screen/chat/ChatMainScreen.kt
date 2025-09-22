package com.example.sumup.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import com.example.sumup.presentation.screen.common.Header
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun ChatMainScreen(
    onChatClick: (String) -> Unit = {}, // callback when user clicks on chat
    onFooterNavigate: (FooterNavigation) -> Unit = {}
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Greeting
            Text(
                text = "Hello,",
                fontSize = 20.sp,
                color = purpleMain
            )
            Text(
                text = "Elon Musk",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Chat list
            ChatItem(
                imageRes = R.drawable.ic_launcher_foreground,
                name = "Johson",
                message = "Thks bro",
                time = "08:12 AM",
                onClick = { onChatClick("Johson") }
            )
            Spacer(modifier = Modifier.height(15.dp))

            ChatItem(
                imageRes = R.drawable.ic_launcher_foreground,
                name = "Hong cheng",
                message = "Hey man, how are you",
                time = "12:12 PM",
                onClick = { onChatClick("Hong cheng") }
            )
            Spacer(modifier = Modifier.height(15.dp))

            ChatItem(
                imageRes = R.drawable.ic_launcher_foreground,
                name = "Bryan Meow",
                message = "Just want to ask you, do you bruiwqqwuohwuohuoqwhouheeqwwqewq",
                time = "",
                hasMessage = true,
                onClick = { onChatClick("Bryan Meow") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Bottom text
            Text(
                text = "End",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ChatItem(
    imageRes: Int,
    name: String,
    message: String,
    time: String,
    hasMessage: Boolean = false,
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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Profile",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Name + message
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = message,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            // Time or unread indicator
            if (hasMessage) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(purpleMain) // purple indicator
                )
            } else {
                Text(
                    text = time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatMainScreenPreview() {
    ChatMainScreen(
        onChatClick = { name ->
            println("Clicked on chat: $name")
        }
    )
}
