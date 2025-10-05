package com.example.sumup.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun AdminAccountsScreen(
    onBack: () -> Unit = {},
    onAccountClick: (userId: String, name: String, email: String, created: String, avatar: String?) -> Unit = 
        { _, _, _, _, _ -> },
) {
    Scaffold(
        topBar = {
            HeaderWithBack(
                title = "Accounts",
                onNavigateBack = onBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Search row
            var query by remember { mutableStateOf("") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search account name here...") },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color(0xFFDBDBDB),
                        unfocusedBorderColor = Color(0xFFE5E5E5),
                        cursorColor = purpleMain
                    )
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Search button
                IconButton(
                    onClick = { /* TODO: hook up search */ },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(purpleMain)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sample account card (static for now). Replace with real Firestore results.
            AccountCard(
                userId = "UC00001",
                name = "Elon Musk",
                email = "chingchong@gmail.com",
                onClick = {
                    onAccountClick("UC00001", "Elon Musk", "chingchong@gmail.com", "22 JAN 2025", null)
                }
            )
        }
    }
}

@Composable
private fun AccountCard(
    userId: String,
    name: String,
    email: String,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .let { it },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = purpleMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .noRippleClickable { onClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFDED7FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = Color(0xFF5F43FF)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = userId, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = name, color = Color.White, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = email, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

// Simple no-ripple clickable modifier to keep card visual per design
@Composable
private fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier =
    this.then(
        androidx.compose.ui.Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() })
        }
    )

@Preview(showBackground = true)
@Composable
fun AdminAccountsScreenPreview() {
    AdminAccountsScreen(
    )
}