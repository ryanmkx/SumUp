package com.example.sumup.presentation.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun AdminAccountDetailScreen(
    userCode: String,
    name: String,
    email: String,
    createdDate: String,
    avatarUrl: String? = null,
    onBack: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
) {
    var showConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            HeaderWithBack(title = userCode, onNavigateBack = onBack)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(18.dp))

            val lightPurple = Color(0xFFE9E4FF)

            InfoRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = purpleMain
                    )
                },
                text = email,
                background = lightPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = purpleMain
                    )
                },
                text = "**********************",
                background = lightPurple
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Schedule,
                        contentDescription = null,
                        tint = purpleMain
                    )
                },
                text = createdDate,
                background = lightPurple
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { showConfirm = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                shape = RoundedCornerShape(14.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = "Delete Account",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    com.example.sumup.presentation.screen.common.ConfirmationPopUp(
        showDialog = showConfirm,
        title = "Confirm Deletion",
        message = "Are you sure you want to delete $name? This action cannot be undone.",
        onConfirm = { showConfirm = false; onDeleteAccount() },
        onDismiss = { showConfirm = false }
    )
}

@Composable
private fun InfoRow(
    icon: @Composable () -> Unit,
    text: String,
    background: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp),
            contentAlignment = Alignment.Center
        ) { icon() }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = Color(0xFF5F43FF),
            fontSize = 14.sp
        )
    }
}

