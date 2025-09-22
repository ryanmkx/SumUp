package com.example.sumup.presentation.screen.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain


@Composable
fun FlashcardStartScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Flashcard"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card fills width automatically
            Card(
                modifier = Modifier
                    .fillMaxWidth()                // take full width
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clickable { //TODO
                        // 👉 Handle click here (e.g., navigate or start the flashcards)
                    },   // side margins
                colors = CardDefaults.cardColors(containerColor = purpleMain),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth() // take full width
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.flashcard),
                        contentDescription = "Flashcard Icon",
                        modifier = Modifier.size(100.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Text
                    Text(
                        text = "Click me to Begin",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardMainScreenPreview() {
    FlashcardStartScreen()
}
