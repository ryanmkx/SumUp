package com.example.sumup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sumup.presentation.screen.login.MainScreen
import com.example.sumup.presentation.screen.login.LoginScreen
import com.example.sumup.presentation.screen.login.SignUpScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeMainScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeResultScreen
import com.example.sumup.presentation.screen.profile.ProfileMainScreen
import com.example.sumup.presentation.screen.profile.EditProfileScreen
import com.example.sumup.presentation.screen.history.HistoryMainScreen
import com.example.sumup.presentation.screen.history.HistoryDetailScreen
import com.example.sumup.presentation.screen.chat.ChatMainScreen
import com.example.sumup.presentation.screen.chat.ChatDetailScreen
import com.example.sumup.presentation.screen.quiz.QuizQuestionScreen
import com.example.sumup.presentation.screen.quiz.QuizResultScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardCardScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardResultScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardStartScreen
import com.example.sumup.presentation.screen.quiz.QuizFlashcardMainScreen
import com.example.sumup.presentation.screen.quiz.QuizStartScreen

object Routes {
    const val MAIN = "main"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD_CODE = "forgot_password_code"
    const val FORGOT_PASSWORD_EMAIL = "forgot_password_email"

    const val SUMMARIZE_MAIN = "summarize_main"
    const val SUMMARIZE_RESULT = "summarize_result"

    const val QUIZ_FLASHCARD_MAIN = "quiz_flashcard_main"
    const val QUIZ_START = "quiz_start"
    const val QUIZ_QUESTION = "quiz_question"
    const val QUIZ_RESULT = "quiz_result"
    const val QUIZ_REVIEW = "quiz_review"

    const val FLASHCARD_START = "flashcard_start"
    const val FLASHCARD_CARD = "flashcard_card"
    const val FLASHCARD_RESULT = "flashcard_result"

    const val PROFILE_MAIN = "profile_main"
    const val PROFILE_EDIT = "profile_edit"

    const val HISTORY_MAIN = "history_main"
    const val HISTORY_DETAIL = "history_detail"

    const val CHAT_MAIN = "chat_main"
    const val CHAT_DETAIL = "chat_detail"
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.MAIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Routes.MAIN) {
            MainScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.SIGN_UP) }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onForgotPassword = { /* TODO: route later */ },
                onLogin = {
                    // After successful login, go to SummarizeMain and clear Login from back stack
                    navController.navigate(Routes.SUMMARIZE_MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onCreateAccount = { navController.navigate(Routes.SIGN_UP) }
            )
        }
        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignUp = { /* TODO: handle sign up then navigate as needed */ },
                onAlreadyHaveAccount = {
                    // Navigate back to Login if present, otherwise navigate to it
                    val popped = navController.popBackStack(Routes.LOGIN, inclusive = false)
                    if (!popped) {
                        navController.navigate(Routes.LOGIN) { launchSingleTop = true }
                    }
                }
            )
        }
        composable(Routes.SUMMARIZE_MAIN) {
            SummarizeMainScreen(
                onSummarize = { _ ->
                    navController.navigate(Routes.SUMMARIZE_RESULT)
                },
                onFooterNavigate = { dest ->
                    when (dest) {
                        com.example.sumup.presentation.screen.common.FooterNavigation.Quiz ->
                            navController.navigate(Routes.QUIZ_FLASHCARD_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Chat ->
                            navController.navigate(Routes.CHAT_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Summarize ->
                            navController.navigate(Routes.SUMMARIZE_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.History ->
                            navController.navigate(Routes.HISTORY_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Profile ->
                            navController.navigate(Routes.PROFILE_MAIN) { launchSingleTop = true }
                    }
                }
            )
        }
        composable(Routes.SUMMARIZE_RESULT) {
            SummarizeResultScreen(
                onBack = { navController.popBackStack() },
                onFlashcard = { navController.navigate(Routes.FLASHCARD_START) },
                onQuiz = { navController.navigate(Routes.QUIZ_START) },
                onCopy = { /* TODO: copy handled in screen */ }
            )
        }
        composable(Routes.QUIZ_FLASHCARD_MAIN) {
            // Route the footer's Quiz tab to the flashcard hub as requested
            QuizFlashcardMainScreen(
                onFooterNavigate = { dest ->
                    when (dest) {
                        com.example.sumup.presentation.screen.common.FooterNavigation.Quiz ->
                            navController.navigate(Routes.QUIZ_FLASHCARD_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Chat ->
                            navController.navigate(Routes.CHAT_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Summarize ->
                            navController.navigate(Routes.SUMMARIZE_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.History ->
                            navController.navigate(Routes.HISTORY_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Profile ->
                            navController.navigate(Routes.PROFILE_MAIN) { launchSingleTop = true }
                    }
                }
            )
        }
        composable(Routes.QUIZ_START) {
            QuizStartScreen()
        }
        composable(Routes.QUIZ_QUESTION) {
            QuizQuestionScreen()
        }
        composable(Routes.QUIZ_RESULT) {
            QuizResultScreen()
        }
        composable(Routes.FLASHCARD_START) {
            FlashcardStartScreen()
        }
        composable(Routes.FLASHCARD_CARD) {
            FlashcardCardScreen()
        }
        composable(Routes.FLASHCARD_RESULT) {
            FlashcardResultScreen()
        }
        composable(Routes.PROFILE_MAIN) {
            ProfileMainScreen(
                onEditProfile = { navController.navigate(Routes.PROFILE_EDIT) },
                onLogout = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onFooterNavigate = { dest ->
                    when (dest) {
                        com.example.sumup.presentation.screen.common.FooterNavigation.Quiz ->
                            navController.navigate(Routes.QUIZ_FLASHCARD_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Chat ->
                            navController.navigate(Routes.CHAT_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Summarize ->
                            navController.navigate(Routes.SUMMARIZE_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.History ->
                            navController.navigate(Routes.HISTORY_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Profile ->
                            navController.navigate(Routes.PROFILE_MAIN) { launchSingleTop = true }
                    }
                }
            )
        }
        composable(Routes.PROFILE_EDIT) {
            EditProfileScreen(
                onUpdate = { _, _, _ ->
                    // After updating, return to Profile main
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.HISTORY_MAIN) {
            HistoryMainScreen(
                onFooterNavigate = { dest ->
                    when (dest) {
                        com.example.sumup.presentation.screen.common.FooterNavigation.Quiz ->
                            navController.navigate(Routes.QUIZ_FLASHCARD_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Chat ->
                            navController.navigate(Routes.CHAT_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Summarize ->
                            navController.navigate(Routes.SUMMARIZE_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.History ->
                            navController.navigate(Routes.HISTORY_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Profile ->
                            navController.navigate(Routes.PROFILE_MAIN) { launchSingleTop = true }
                    }
                },
                onHistoryClick = { date, content ->
                    val encodedDate = java.net.URLEncoder.encode(date, Charsets.UTF_8.name())
                    val encodedContent = java.net.URLEncoder.encode(content, Charsets.UTF_8.name())
                    navController.navigate(Routes.HISTORY_DETAIL + "?date=" + encodedDate + "&content=" + encodedContent)
                }
            )
        }
        composable(
            route = Routes.HISTORY_DETAIL + "?date={date}&content={content}",
            arguments = listOf(
                navArgument("date") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("content") { type = NavType.StringType; nullable = true; defaultValue = "" },
            )
        ) { backStackEntry ->
            val rawDate = backStackEntry.arguments?.getString("date") ?: ""
            val rawContent = backStackEntry.arguments?.getString("content") ?: ""
            val date = java.net.URLDecoder.decode(rawDate, Charsets.UTF_8.name())
            val content = java.net.URLDecoder.decode(rawContent, Charsets.UTF_8.name())
            HistoryDetailScreen(
                date = date,
                content = content,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.CHAT_MAIN) {
            ChatMainScreen(
                onFooterNavigate = { dest ->
                    when (dest) {
                        com.example.sumup.presentation.screen.common.FooterNavigation.Quiz ->
                            navController.navigate(Routes.QUIZ_FLASHCARD_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Chat ->
                            navController.navigate(Routes.CHAT_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Summarize ->
                            navController.navigate(Routes.SUMMARIZE_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.History ->
                            navController.navigate(Routes.HISTORY_MAIN) { launchSingleTop = true }
                        com.example.sumup.presentation.screen.common.FooterNavigation.Profile ->
                            navController.navigate(Routes.PROFILE_MAIN) { launchSingleTop = true }
                    }
                }
            )
        }
        composable(Routes.CHAT_DETAIL) {
            ChatDetailScreen()
        }
    }
}


