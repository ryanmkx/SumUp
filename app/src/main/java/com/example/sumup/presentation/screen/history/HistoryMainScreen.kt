package com.example.sumup.presentation.screen.history


import androidx.compose.foundation.layout.*
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
import com.example.sumup.presentation.screen.ui.purpleMain
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import com.example.sumup.presentation.screen.common.Header


@Composable
fun HistoryMainScreen() {
    val historyList = listOf(
        Pair("26 June 2025", "First Summarizer content is this one ya, look here to see more information. Click this butwerwebasdHIBFEWUYFGWEFUYWEGFYUWEGFUWE   FGWEYUDFGWE YUFDDSSFD..."),
        Pair("03 May 2025", "Computer Science is a big circle that contain a lot of others component like math, IT and...")
    )

    Scaffold(
        topBar = {
            Header(title = "History")
        },
        bottomBar = {
            FooterBar(
                currentRoute = FooterNavigation.History,
                onNavigate = {}
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),   // 👈 spacing from screen edge
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(historyList) { (date, content) ->
                    HistoryCard(
                        date = date,
                        content = content,
                        onClick = { println("Clicked on $date") }
                    )
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


@Preview(showBackground = true)
@Composable
fun PreviewHistoryMainScreen() {
    HistoryMainScreen()
}
