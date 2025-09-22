package com.example.sumup.presentation.screen.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBackAndPic
import com.example.sumup.presentation.ui.theme.purpleMain

data class ChatMessage(
    val text: String,
    val time: String,
    val isSent: Boolean, // true = my message, false = received
    val seen: Boolean = false // ✅ new property for ticks
)

@Composable
fun ChatDetailScreen(
    userName: String = "Johson",
    userImage: Int = R.drawable.profile_pic,
    onBack: () -> Unit = {}
) {
    var message by remember { mutableStateOf(TextFieldValue("")) }

    val messages = remember {
        mutableStateListOf(
            ChatMessage("Hi man, how are you doing?", "08:12 AM", false),
            ChatMessage("Hey brooo, good good good", "08:13 AM", true, seen = true),
            ChatMessage("Cool, talk later!", "08:15 AM", true, seen = false)
        )
    }

    Scaffold(
        topBar = {
            HeaderWithBackAndPic(
                title = userName,
                onNavigateBack = onBack,
                profileImageRes = userImage
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ✅ Transparent background wallpaper
            Image(
                painter = painterResource(id = R.drawable.chat_background),
                contentDescription = "Chat Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.3f
            )

            // ✅ Messages
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messages.reversed()) { msg ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = if (msg.isSent) Alignment.End else Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(
                                    if (msg.isSent) purpleMain.copy(alpha = 0.9f)   // ✅ My messages = purple bubble
                                    else Color(0xFFF2F2F7).copy(alpha = 0.9f)       // ✅ Others = soft gray bubble
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = if (msg.isSent) Color.White else Color.Black, // ✅ White text on purple, black on gray
                                fontSize = 15.sp
                            )
                        }

                        // ✅ Time + Tick Row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 2.dp, start = 4.dp, end = 4.dp)
                        ) {
                            Text(
                                text = msg.time,
                                fontSize = 10.sp,
                                color = Color.Gray
                            )

                            if (msg.isSent) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (msg.seen) "✓✓" else "✓",
                                    fontSize = 10.sp,
                                    color = if (msg.seen) Color(0xFF1DA1F2) else Color.Gray // ✅ blue if seen
                                )
                            }
                        }
                    }
                }
            }

            // ✅ Floating Input Bar
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.05f)) // translucent floating bar
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Type a message...", color = Color.Gray) },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.colors( // ✅ Material3 version
                        focusedContainerColor = Color.White.copy(alpha = 0.6f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.6f),
                        disabledContainerColor = Color.White.copy(alpha = 0.3f),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = purpleMain
                    )
                )
                IconButton(
                    onClick = {
                        if (message.text.isNotBlank()) {
                            messages.add(ChatMessage(message.text, "Now", true, seen = false))
                            message = TextFieldValue("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = purpleMain
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatDetailScreenPreview() {
    ChatDetailScreen()
}
