package com.example.sumup.presentation.screen.history

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.purpleMain


@Composable
fun HistoryDetailScreen(
    date: String,
    content: String,
    onBack: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onBack,
                title = "History Detail"
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

            // Card fills width automatically
            HistoryDetailCard(
                date = date,
                content = content
            )
        }
    }
}

@Composable
fun HistoryDetailCard(
    date: String,
    content: String,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    Card(
        modifier = modifier
            .fillMaxWidth()                 // take full width
            .padding(horizontal = 16.dp),   // side margins
        colors = CardDefaults.cardColors(containerColor = purpleMain),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Date Row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.history_white_icon),
                    contentDescription = "History Icon",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = date,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Content Text
            Text(
                text = content,
                color = Color.White,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                overflow = TextOverflow.Ellipsis,   // adds "…" when text is too long
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Copy Button at bottom-right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(content))
                        Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.copy_icon),
                        contentDescription = "Copy",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}



private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun HistoryDetailScreenPreview() {
    HistoryDetailScreen(
        date = "26 June 2025",
        content = "First Summarizer content is this one ya, look here to see more information. " +
                "Click this button to perform it. For example There are times when the night sky glows " +
                "with bands of color..."
    )
}
