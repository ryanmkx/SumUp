package com.example.sumup.presentation.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun QuizQuestionScreen(
    onBack: () -> Unit = {},
    progress: Float = 0.2f,
    currentIndex: Int = 1,
    total: Int = 5,
    question: String = "What is the meaning of Computer ?",
    answers: List<String> = listOf(
        "An electronic device for storing and processing data",
        "An electric device for storing and processing data",
        "An electronic device for playing games",
        "A physical device for storing data"
    ),
    correctAnswer: String = "An electronic device for storing and processing data"
) {
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Questions"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Progress row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.question_icon),
                    contentDescription = "Quiz Icon",
                    tint = purpleMain,
                    modifier = Modifier.size(24.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Question box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(purpleMain.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(24.dp)
            ) {
                Text(
                    text = question,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Answer options
            answers.forEachIndexed { index, answer ->
                val isSelected = selectedAnswer == answer
                val isCorrect = answer == correctAnswer
                val backgroundColor =
                    if (isSelected) {
                        if (isCorrect) Color(0xFF7EE98B) // Green
                        else Color(0xFFFFA0A0) // Red
                    } else {
                        Color.White
                    }

                AnswerOption(
                    label = ('A' + index).toString(),
                    text = answer,
                    backgroundColor = backgroundColor,
                    onClick = {
                        if (selectedAnswer == null) { // Only allow first selection
                            selectedAnswer = answer
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun AnswerOption(
    label: String,
    text: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizQuestionScreenPreview() {
    QuizQuestionScreen()
}
