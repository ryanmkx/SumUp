package com.example.sumup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sumup.presentation.screen.flashcard.FlashcardCardScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardMainScreen
import com.example.sumup.presentation.screen.history.HistoryDetailScreen
import com.example.sumup.presentation.screen.history.HistoryMainScreen
import com.example.sumup.presentation.screen.login.LoginScreen
import com.example.sumup.presentation.screen.login.MainScreen
import com.example.sumup.presentation.screen.login.SignUpScreen
import com.example.sumup.presentation.screen.profile.EditProfileScreen
import com.example.sumup.presentation.screen.profile.ProfileMainScreen
import com.example.sumup.presentation.screen.quiz.QuizMainScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeMainScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeResultScreen
import com.example.sumup.presentation.ui.theme.SumUpTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SumUpTheme {
//                SummarizeMainScreen()
//                EditProfileScreen()
//                ProfileMainScreen()
//                SummarizeResultScreen()
//                MainScreen(onLoginClick = {},
//                    onRegisterClick = {}
//                )
//                LoginScreen()
//                SignUpScreen()
//                HistoryDetailScreen(
//                    date = "26 June 2025",
//                    content = "First Summarizer content is this one ya, look here to see more information. " +
//                            "Click this button to perform it. For example There are times when the night sky glows " +
//                            "with bands of color..."
//                    )
//                HistoryMainScreen()
//                FlashcardMainScreen()
                QuizMainScreen()
                FlashcardCardScreen()
            }
        }
    }
}
