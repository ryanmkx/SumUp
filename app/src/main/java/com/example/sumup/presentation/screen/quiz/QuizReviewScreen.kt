package com.example.sumup.presentation.screen.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain

// --- Data model ---
data class ReviewItem(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String,
    val selectedAnswer: String?
)

@Composable
fun QuizReviewScreen(
    onBack: () -> Unit = {},
    items: List<ReviewItem>
) {
    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "Review Answers"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            items.forEachIndexed { index, item ->
                QuestionReviewCard(
                    index = index + 1,
                    item = item
                )
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun QuestionReviewCard(
    index: Int,
    item: ReviewItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = purpleMain.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = "Question $index",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = purpleMain
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.question,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(12.dp))

        item.answers.forEachIndexed { idx, answer ->
            val isCorrect = answer == item.correctAnswer
            val isSelected = answer == item.selectedAnswer
            val bgColor = when {
                isCorrect -> Color(0xFFB2F2BB)       // Green for correct
                isSelected && !isCorrect -> Color(0xFFFFA8A8) // Red for wrong selected
                else -> Color.White
            }

            AnswerReviewOption(
                label = ('A' + idx).toString(),
                text = answer,
                backgroundColor = bgColor
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AnswerReviewOption(
    label: String,
    text: String,
    backgroundColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizReviewScreenPreview() {
    val sampleItems = listOf(
        ReviewItem(
            question = "What is the meaning of Computer?",
            answers = listOf(
                "An electronic device for storing and processing data",
                "An electric device for storing and processing data",
                "An electronic device for playing games",
                "A physical device for storing data"
            ),
            correctAnswer = "An electronic device for storing and processing data",
            selectedAnswer = "An electronic device for playing games"
        ),
        ReviewItem(
            question = "CPU stands for?",
            answers = listOf(
                "Central Processing Unit",
                "Control Processing Unit",
                "Central Printed Unit",
                "Central Process Utility"
            ),
            correctAnswer = "Central Processing Unit",
            selectedAnswer = "Central Processing Unit"
        )
    )
    QuizReviewScreen(items = sampleItems)
}
