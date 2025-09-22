package com.example.sumup.presentation.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun QuizResultScreen(
    onBack: () -> Unit = {},
    onNext: () -> Unit = {},
    onReview: () -> Unit = {}, // ✅ Review Answers action
    progress: Float = 1f,
    currentIndex: Int = 5,
    total: Int = 5,
    score: Int = 4
) {
    Scaffold(
        topBar = {
            Header(
                title = "Quiz"
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
            // Progress Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.question_icon),
                    contentDescription = "Quiz Icon",
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

            // Result card
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .background(purpleMain, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(1.dp))

                    // ✅ Middle content (trophy + texts)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.trophy_icon),
                            contentDescription = "Trophy Icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = "Congratulations",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Your Score",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // ✅ Score display
                        Text(
                            text = "$score / $total",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Yellow,
                            textAlign = TextAlign.Center,
                        )

                        Spacer(modifier = Modifier.height(15.dp))

                        Text(
                            text = "You did a good job. Continue learning!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                    }

                    // ✅ Bottom Review Answers
                    Text(
                        text = "Click to Review Answers",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp)
                            .clickable { onReview() }
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
                    onClick = onBack,
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
                    onClick = onNext,
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
fun QuizResultScreenPreview() {
    QuizResultScreen()
}
