package com.example.sumup.presentation.screen.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import com.example.sumup.presentation.screen.common.Header
import com.example.sumup.presentation.screen.ui.purpleMain

data class QuizFlashcardItem(
    val title: String,
    val date: String,
    val score: String,
    val isQuiz: Boolean
)

@Composable
fun QuizFlashcardMainScreen(
    onFooterNavigate: (FooterNavigation) -> Unit = {},
    entries: List<QuizFlashcardItem> = sampleEntries
) {
    Scaffold(
        topBar = { Header(title = "Activities") },
        bottomBar = {
            FooterBar(
                currentRoute = FooterNavigation.Quiz,
                onNavigate = onFooterNavigate
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // ---- Quizzes section header ----
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.question_icon),
                        contentDescription = "quiz icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "All Quizzes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }

            // ---- Quiz items ----
            val quizzes = entries.filter { it.isQuiz }
            items(quizzes) { quiz ->
                ActivityCard(item = quiz) {
                    // handle click for quiz (navigate to review/result)
                }
            }

            // ---- Flashcards header ----
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cards),
                        contentDescription = "flashcard icon",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "All Flashcards",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )
                }
            }

            // ---- Flashcard items ----
            val flashcards = entries.filter { !it.isQuiz }
            items(flashcards) { flash ->
                ActivityCard(item = flash) {
                    // handle click for flashcard
                }
            }
        }
    }
}

@Composable
fun ActivityCard(
    item: QuizFlashcardItem,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = purpleMain),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.date,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
            }

            Text(
                text = item.score,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

private val sampleEntries = listOf(
    QuizFlashcardItem("Title", "22 MAR 2025", "3/5", true),
    QuizFlashcardItem("Title", "22 MAR 2025", "3/5", false),
    QuizFlashcardItem("Title 2", "01 APR 2025", "4/5", true)
)

@Preview(showBackground = true)
@Composable
fun QuizFlashcardMainScreenPreview() {
    QuizFlashcardMainScreen()
}
