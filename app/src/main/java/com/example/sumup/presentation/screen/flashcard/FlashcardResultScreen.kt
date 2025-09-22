package com.example.sumup.presentation.screen.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.Header
import com.example.sumup.presentation.ui.theme.greenTick
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun FlashcardResultScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    progress: Float = 1f,
    currentIndex: Int = 5,
    total: Int = 5,
) {
    Scaffold(
        topBar = {
            Header(
                title = "Flashcard"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cards),
                    contentDescription = "Flashcard Icon",
                    tint = purpleMain,
                    modifier = Modifier.size(25.dp)
                )

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(12.dp)
                        .padding(horizontal = 8.dp),
                    color = Color(0xFFFFC107),
                    trackColor = Color(0xFFFFEBB2)
                )

                Text(
                    text = "$currentIndex/$total",
                    fontSize = 18.sp,
                    color = purpleMain,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .matchParentSize()
                        .background(purpleMain, RoundedCornerShape(16.dp))
                        .padding(10.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.tick_icon),
                        contentDescription = "Tick Icon",
                        tint = greenTick,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "Congratulations",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "You've reviewed all the flashcards",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            // Navigation buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onBack, // TODO
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Again",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onNext, // TODO
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Home",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardResultScreenPreview() {
    FlashcardResultScreen()
}
