package com.example.sumup.presentation.screen.flashcard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun FlashcardCardScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    progress: Float = 0.2f,
    currentIndex: Int = 1,
    total: Int = 5,
    frontText: String = "Computer",
    backText: String = "An electronic device that processes data."
) {
    var isFlipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "cardRotation"
    )

    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Flashcards"
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

            // Flashcard with flip animation
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .clickable { isFlipped = !isFlipped },
                contentAlignment = Alignment.Center
            ) {
                // Front side
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12 * density
                            alpha = if (rotation <= 90f) 1f else 0f
                        }
                        .background(purpleMain, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FlashcardContent(text = frontText)
                }

                // Back side
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .graphicsLayer {
                            rotationY = rotation + 180f
                            cameraDistance = 12 * density
                            alpha = if (rotation > 90f) 1f else 0f
                        }
                        .background(purpleMain, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FlashcardContent(text = backText)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "*Click to flip*",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray.copy(alpha = 0.8f)
                )

            Spacer(modifier = Modifier.height(30.dp))

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
                    Text("Back",
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
                        text = "Next",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )                }
            }
        }
    }
}

@Composable
fun FlashcardContent(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardCardScreenPreview() {
    FlashcardCardScreen()
}
