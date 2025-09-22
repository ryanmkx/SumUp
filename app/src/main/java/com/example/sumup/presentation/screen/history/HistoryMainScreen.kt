package com.example.sumup.presentation.screen.history


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.ui.theme.purpleMain
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import com.example.sumup.presentation.screen.common.Header
import com.example.sumup.presentation.viewModel.HistoryViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun HistoryMainScreen(
    viewModel: HistoryViewModel,
    onFooterNavigate: (FooterNavigation) -> Unit = {},
    onHistoryClick: (String, String) -> Unit = { _, _ -> }
) {
    val summaries by viewModel.summaries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    // Helper function to format timestamp to date string
    fun formatTimestamp(timestamp: Timestamp?): String {
        if (timestamp == null) return "Unknown date"
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    Scaffold(
        topBar = {
            Header(title = "History")
        },
        bottomBar = {
            FooterBar(
                currentRoute = FooterNavigation.History,
                onNavigate = onFooterNavigate
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = purpleMain)
                    }
                }
                error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error: $error",
                                color = Color.Red,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.refreshSummaries() },
                                colors = ButtonDefaults.buttonColors(containerColor = purpleMain)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                summaries.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No summaries found",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(summaries) { summary ->
                            HistoryCard(
                                date = formatTimestamp(summary.createdAt),
                                content = summary.summaryText,
                                onClick = { onHistoryClick(formatTimestamp(summary.createdAt), summary.summaryText) }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun HistoryCard(
    date: String,
    content: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = purpleMain),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.history_white_icon),
                    contentDescription = "History Icon",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = content,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun PreviewHistoryMainScreen() {
//    HistoryMainScreen()
//}
