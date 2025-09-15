package com.example.sumup.presentation.screen.textSummarizer

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.screen.ui.purpleMain

@Composable
fun SummarizeResultScreen(
    summarizedText: String = "The Aurora Realise, or Northern Lights, are bands of color in the night sky. Ancient people thought these lights were dragons on fire, and even modern scientists are not sure what they are." +
            "Nursing Practitioners make up the bulk of healthcare providers in the USA. They are very versatile and can be found in any area and healthcare setting. They practice acute care, outpatient care, and many other specialties. If required, they can serve in positions of management and leadership, conduct research on affiliated topics and matters, and serve as teachers in nursing faculties and healthcare organizations. In terms of practice, their role resembles that of doctors (Painter, n.d.). They have undergone advanced medical training in order to provide high-quality healthcare. Once a student graduates and receives the title of Nurse Practitioner, there are plenty of opportunities to follow and goals to pursue. The purpose of this case study is to identify organizations where one can work as a healthcare professional.",
    onBack: () -> Unit = {},
    onFlashcard: () -> Unit = {},
    onQuiz: () -> Unit = {},
    onCopy: () -> Unit = {}
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            HeaderWithBack(
                title = "Result",
                onNavigateBack = onBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // === Card with text & copy button ===
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.80f),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Scrollable text content
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = summarizedText,
                                fontSize = 16.sp,
                                lineHeight = 22.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Start
                            )
                        }
                    }
                    
                    // Copy button row at the bottom
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = {
                                onCopy()
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.copy_icon),
                                contentDescription = "Copy text",
                                tint = purpleMain,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // === Flashcard & Quiz buttons ===
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Flashcard",
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 60.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Text(
                        text = "Quiz",
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
fun SummarizeResultScreenPreview() {
    SummarizeResultScreen()
}
