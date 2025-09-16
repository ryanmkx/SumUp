package com.example.sumup.presentation.screen.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.ui.purpleMain

sealed class FooterNavigation {
    object Quiz : FooterNavigation()
    object Chat : FooterNavigation()
    object Summarize : FooterNavigation()
    object History : FooterNavigation()
    object Profile : FooterNavigation()
}

@Composable
fun FooterBar(
    currentRoute: FooterNavigation = FooterNavigation.Quiz,
    onNavigate: (FooterNavigation) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background // ✅ match scaffold
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top divider line
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f), // ✅ subtle divider
                thickness = 1.dp
            )

            // Row for active line indicators
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(
                    FooterNavigation.Quiz,
                    FooterNavigation.Chat,
                    FooterNavigation.Summarize,
                    FooterNavigation.History,
                    FooterNavigation.Profile
                ).forEach { navItem ->
                    Box(
                        modifier = Modifier.width(72.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        if (currentRoute == navItem) {
                            Box(
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(3.dp)
                                    .background(purpleMain) // ✅ consistent highlight
                            )
                        }
                    }
                }
            }

            // Row for icons and labels
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 14.dp)
            ) {
                FooterIcon(
                    iconRes = R.drawable.quiz_icon,
                    label = "Q&A/CARD",
                    isActive = currentRoute == FooterNavigation.Quiz
                ) { onNavigate(FooterNavigation.Quiz) }

                FooterIcon(
                    iconRes = R.drawable.chat_icon,
                    label = "CHAT",
                    isActive = currentRoute == FooterNavigation.Chat
                ) { onNavigate(FooterNavigation.Chat) }

                FooterIcon(
                    iconRes = R.drawable.summarize_icon,
                    label = "SUMMARIZE",
                    isActive = currentRoute == FooterNavigation.Summarize
                ) { onNavigate(FooterNavigation.Summarize) }

                FooterIcon(
                    iconRes = R.drawable.history_icon,
                    label = "HISTORY",
                    isActive = currentRoute == FooterNavigation.History
                ) { onNavigate(FooterNavigation.History) }

                FooterIcon(
                    iconRes = R.drawable.profile_icon,
                    label = "PROFILE",
                    isActive = currentRoute == FooterNavigation.Profile
                ) { onNavigate(FooterNavigation.Profile) }
            }
        }
    }
}

@Composable
fun FooterIcon(iconRes: Int, label: String, isActive: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .width(72.dp)
            .padding(horizontal = 4.dp)
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier
                .size(25.dp)
                .padding(bottom = 4.dp)
        )
        Text(
            text = label,
            fontSize = 9.sp,
            fontFamily = FontFamily.Default,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal, // ✅ bold when active
            color = if (isActive) purpleMain else MaterialTheme.colorScheme.onBackground, // ✅ adapts to theme
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFooter() {
    FooterBar(
        currentRoute = FooterNavigation.Quiz,
        onNavigate = {}
    )
}
