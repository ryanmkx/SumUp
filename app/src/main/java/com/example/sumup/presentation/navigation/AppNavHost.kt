package com.example.sumup.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.sumup.businessLogic.LogIn
import com.example.sumup.businessLogic.SignUp
import com.example.sumup.businessLogic.UpdateProfile
import com.example.sumup.presentation.viewModel.HistoryViewModel
import com.example.sumup.businessLogic.GetUserSummaries
import com.example.sumup.businessLogic.DependencyModule
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.database.FirebaseDatabase
import com.example.sumup.data.repository.FirebaseAuthRepository
import com.example.sumup.data.repository.FirestoreUserProfileRepository
import com.example.sumup.data.repository.FirestoreSummaryRepository
import com.example.sumup.data.repository.FirebaseChatRepository
import com.example.sumup.presentation.screen.login.MainScreen
import com.example.sumup.presentation.screen.login.LoginScreen
import com.example.sumup.presentation.screen.login.SignUpScreen
import com.example.sumup.presentation.screen.login.ForgotPasswordEmailScreen
import com.example.sumup.presentation.screen.login.ChangePasswordScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeMainScreen
import com.example.sumup.presentation.screen.textSummarizer.SummarizeResultScreen
import com.example.sumup.presentation.screen.profile.ProfileMainScreen
import com.example.sumup.presentation.screen.profile.EditProfileScreen
import com.example.sumup.presentation.screen.history.HistoryMainScreen
import com.example.sumup.presentation.screen.history.HistoryDetailScreen
import com.example.sumup.presentation.screen.chat.ChatMainScreen
import com.example.sumup.presentation.screen.chat.ChatDetailScreen
import com.example.sumup.presentation.screen.chat.AddFriendScreen
import com.example.sumup.presentation.screen.chat.FriendRequestScreen
import com.example.sumup.presentation.screen.quiz.QuizQuestionScreen
import com.example.sumup.presentation.screen.quiz.QuizResultScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardCardScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardResultScreen
import com.example.sumup.presentation.screen.flashcard.FlashcardStartScreen
import com.example.sumup.presentation.screen.quiz.QuizFlashcardMainScreen
import com.example.sumup.presentation.screen.quiz.QuizStartScreen
import kotlinx.coroutines.launch
import com.example.sumup.presentation.screen.admin.AdminAccountsScreen
import com.example.sumup.presentation.screen.admin.AdminAccountDetailScreen
import kotlinx.coroutines.tasks.await

object Routes {
    const val MAIN = "main"
    const val LOGIN = "login"
    const val SIGN_UP = "sign_up"
    const val FORGOT_PASSWORD_EMAIL = "forgot_password_email"
    const val CHANGE_PASSWORD = "change_password"

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
    const val ADD_FRIEND = "add_friend"
    const val FRIEND_REQUESTS = "friend_requests"

    // Admin
    const val ADMIN_HOME = "admin_home"
    const val ADMIN_ACCOUNTS = "admin_accounts"
    const val ADMIN_ACCOUNT_DETAIL = "admin_account_detail"
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
            val context = LocalContext.current
            LaunchedEffect(Unit) {
                val current = FirebaseAuth.getInstance().currentUser
                if (current != null) {
                    // Check profile to determine role and status
                    val getUserProfileUseCase = DependencyModule.getUserProfileUseCase
                    getUserProfileUseCase.execute()
                        .onSuccess { user ->
                            if (user.status.equals("disabled", ignoreCase = true)) {
                                // Sign out disabled accounts and stay on Main
                                DependencyModule.userAuthRepository.signOut()
                                Toast.makeText(context, "This account has been disabled.", Toast.LENGTH_SHORT).show()
                            } else {
                                val destination = if (user.level.lowercase() == "admin") Routes.ADMIN_HOME else Routes.SUMMARIZE_MAIN
                                navController.navigate(destination) {
                                    popUpTo(Routes.MAIN) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        }
                        .onFailure {
                            // If profile fetch fails, remain on Main
                        }
                }
            }
            MainScreen(
                onLoginClick = { navController.navigate(Routes.LOGIN) },
                onRegisterClick = { navController.navigate(Routes.SIGN_UP) }
            )
        }
        // Admin home entry (Manage Accounts)
        composable(Routes.ADMIN_HOME) {
            val auth = remember { DependencyModule.userAuthRepository }
            val context = LocalContext.current
            com.example.sumup.presentation.screen.admin.AdminMainScreen(
                onManageClick = { navController.navigate(Routes.ADMIN_ACCOUNTS) },
                onLogout = {
                    auth.signOut()
                    Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.MAIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            // Use dependency injection instead of creating repositories directly
            val signInUseCase = remember { DependencyModule.logInUseCase }

            LoginScreen(
                onBack = { navController.popBackStack() },
                onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD_EMAIL) },
                onLogin = { email, password ->
                    coroutineScope.launch {
                        val result = signInUseCase.execute(email, password)
                        if (result.isSuccess) {
                            val user = result.getOrNull()
                            val destination = if (user?.level?.lowercase() == "admin") Routes.ADMIN_HOME else Routes.SUMMARIZE_MAIN
                            navController.navigate(destination) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                result.exceptionOrNull()?.message ?: "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onCreateAccount = { navController.navigate(Routes.SIGN_UP) }
            )
        }
        composable(Routes.SIGN_UP) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            // Use dependency injection instead of creating repositories directly
            val signUpUseCase = remember { DependencyModule.signUpUseCase }

            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignUp = { email, username, password ->
                    coroutineScope.launch {
                        val result = signUpUseCase.execute(email, username, password)
                        if (result.isSuccess) {
                            Toast.makeText(
                                context,
                                "Registered successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.SIGN_UP) { inclusive = true }
                                launchSingleTop = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                result.exceptionOrNull()?.message ?: "Registration failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                onAlreadyHaveAccount = {
                    // Navigate back to Login if present, otherwise navigate to it
                    val popped = navController.popBackStack(Routes.LOGIN, inclusive = false)
                    if (!popped) {
                        navController.navigate(Routes.LOGIN) { launchSingleTop = true }
                    }
                }
            )
        }
        
        // Forgot Password Email Screen
        composable(Routes.FORGOT_PASSWORD_EMAIL) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val forgotPasswordUseCase = remember { DependencyModule.forgotPasswordUseCase }
            var email by remember { mutableStateOf("") }
            var isLoading by remember { mutableStateOf(false) }

            ForgotPasswordEmailScreen(
                onBack = { navController.popBackStack() },
                isLoading = isLoading,
                onSendEmail = { emailAddress ->
                    email = emailAddress
                    isLoading = true
                    coroutineScope.launch {
                        val result = forgotPasswordUseCase.sendPasswordResetEmail(emailAddress)
                        isLoading = false
                        if (result.isSuccess) {
                            Toast.makeText(
                                context,
                                "Password reset email sent! Check your inbox and follow the link to reset your password.",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.FORGOT_PASSWORD_EMAIL) { inclusive = true }
                            }
                        } else {
                            val errorMessage = result.exceptionOrNull()?.message ?: "Failed to send reset email"
                            Toast.makeText(
                                context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )
        }
        
        // Change Password Screen
        composable(Routes.CHANGE_PASSWORD) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            val changePasswordUseCase = remember { DependencyModule.changePasswordUseCase }
            var isLoading by remember { mutableStateOf(false) }

            ChangePasswordScreen(
                onBack = { navController.popBackStack() },
                onPasswordChanged = { currentPassword, newPassword ->
                    isLoading = true
                    coroutineScope.launch {
                        val result = changePasswordUseCase.execute(currentPassword, newPassword)
                        isLoading = false
                        if (result.isSuccess) {
                            Toast.makeText(
                                context,
                                "Password changed successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.popBackStack()
                        } else {
                            val errorMessage = result.exceptionOrNull()?.message ?: "Failed to change password"
                            Toast.makeText(
                                context,
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                        }
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
        // Admin accounts list
        composable(Routes.ADMIN_ACCOUNTS) {
            AdminAccountsScreen(
                onBack = { navController.popBackStack() },
                onAccountClick = { userId, name, email, created, avatar ->
                    val enc = { s: String -> java.net.URLEncoder.encode(s, Charsets.UTF_8.name()) }
                    val route = Routes.ADMIN_ACCOUNT_DETAIL +
                        "?userId=" + enc(userId) +
                        "&name=" + enc(name) +
                        "&email=" + enc(email) +
                        "&created=" + enc(created) +
                        "&avatar=" + enc(avatar ?: "")
                    navController.navigate(route)
                }
            )
        }
        // Admin account detail with required args
        composable(
            route = Routes.ADMIN_ACCOUNT_DETAIL + "?userId={userId}&name={name}&email={email}&created={created}&avatar={avatar}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType; defaultValue = "" },
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("email") { type = NavType.StringType; defaultValue = "" },
                navArgument("created") { type = NavType.StringType; defaultValue = "" },
                navArgument("avatar") { type = NavType.StringType; defaultValue = "" },
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val name = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("name") ?: "", Charsets.UTF_8.name())
            val email = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("email") ?: "", Charsets.UTF_8.name())
            val created = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("created") ?: "", Charsets.UTF_8.name())
            val avatar = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("avatar") ?: "", Charsets.UTF_8.name())

            // Compose-scope values
            val deleteUseCase = remember { DependencyModule.deleteUserAccountUseCase }
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            AdminAccountDetailScreen(
                userCode = userId,
                name = name,
                email = email,
                createdDate = created,
                avatarUrl = avatar.ifEmpty { null },
                onBack = { navController.popBackStack() },
                onDeleteAccount = {
                    // Use DI to disable the user and then navigate back
                    scope.launch {
                        val res = deleteUseCase.execute(userId)
                        if (res.isSuccess) {
                            Toast.makeText(context, "Account disabled", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        } else {
                            Toast.makeText(context, res.exceptionOrNull()?.message ?: "Disable failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
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
            val context = LocalContext.current
            // Use dependency injection instead of creating repositories directly
            val authRepo = remember { DependencyModule.userAuthRepository }
            val getUserProfileUseCase = remember { DependencyModule.getUserProfileUseCase }
            var userName by remember { mutableStateOf("") }
            var email by remember { mutableStateOf("") }
            var profilePicUrl by remember { mutableStateOf("") }

            // Load profile data when screen is first displayed and when returning from other screens
            LaunchedEffect(navController.currentBackStackEntry) {
                getUserProfileUseCase.execute()
                    .onSuccess { user ->
                        userName = user.username
                        email = user.email
                        profilePicUrl = user.profilePic
                    }
                    .onFailure { exception ->
                        Toast.makeText(
                            context,
                            exception.message ?: "Failed to load profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            ProfileMainScreen(
                userName = if (userName.isNotEmpty()) userName else "",
                email = if (email.isNotEmpty()) email else "",
                profilePicUrl = profilePicUrl,
                onEditProfile = { navController.navigate(Routes.PROFILE_EDIT) },
                onChangePassword = { navController.navigate(Routes.CHANGE_PASSWORD) },
                onLogout = {
                    authRepo.signOut()
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
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            // Use dependency injection instead of creating repositories directly
            val updateProfileUseCase = remember { DependencyModule.updateProfileUseCase }

            EditProfileScreen(
                onUpdate = { username ->
                    // The profile has already been updated inside EditProfileScreen via its ViewModel.
                    // Avoid triggering a second update here that would reset status to default.
                    Toast.makeText(
                        context,
                        "Profile updated successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Routes.HISTORY_MAIN) {
            // Use dependency injection instead of creating repositories directly
            val historyViewModel = remember { DependencyModule.createHistoryViewModel() }
            
            HistoryMainScreen(
                viewModel = historyViewModel,
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
            // Load chat rooms that include the current user and map to UI items
            var friendsUi by remember { mutableStateOf<List<com.example.sumup.presentation.screen.chat.ChatFriend>>(emptyList()) }
            var unreadCounts by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
            var userName by remember { mutableStateOf("User") } // Current user's name
            // Use dependency injection instead of creating repositories directly
            val chatRepo = remember { DependencyModule.chatRepository }
            val getUserProfileUseCase = remember { DependencyModule.getUserProfileUseCase }
            val context = LocalContext.current
            
            // Load current user's name and chat data when screen is first displayed and when returning from other screens
            LaunchedEffect(navController.currentBackStackEntry) {
                getUserProfileUseCase.execute()
                    .onSuccess { user ->
                        userName = user.username
                    }
                    .onFailure { exception ->
                        println("Failed to load user profile: ${exception.message}")
                        userName = "User" // fallback
                    }
                
                // Load chat rooms data
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    try {
                        // Use Firebase Realtime Database (not Firestore) for chat rooms
                        val realtimeDb = FirebaseDatabase.getInstance("https://sumup-31d9b-default-rtdb.asia-southeast1.firebasedatabase.app")
                        val chatRoomsRef = realtimeDb.reference.child("chatRooms")
                        
                        // Get all chat rooms
                        val snapshot = chatRoomsRef.get().await()
                        val roomUsers = mutableListOf<com.example.sumup.presentation.screen.chat.ChatFriend>()
                        
                        for (roomSnapshot in snapshot.children) {
                            val participants = roomSnapshot.child("participants").getValue(object : com.google.firebase.database.GenericTypeIndicator<List<String>>() {})
                            
                            if (participants != null && participants.contains(currentUserId)) {
                                val otherUserId = participants.firstOrNull { it != currentUserId }
                                if (otherUserId != null) {
                                    // Get user info from Firestore
                                    val firestoreDb = FirebaseFirestore.getInstance()
                                    val otherDoc = firestoreDb.collection("Users").document(otherUserId).get().await()
                                    val otherName = otherDoc.getString("username") ?: "Unknown"
                                    val otherProfilePic = otherDoc.getString("profilePic") ?: ""
                                    
                                    roomUsers.add(
                                        com.example.sumup.presentation.screen.chat.ChatFriend(
                                            name = otherName,
                                            hasUnreadMessage = false,
                                            profilePicUrl = otherProfilePic,
                                            userId = otherUserId
                                        )
                                    )
                                }
                            }
                        }
                        friendsUi = roomUsers
                    } catch (e: Exception) {
                        println("Error loading chat rooms: ${e.message}")
                        friendsUi = emptyList()
                    }
                } else {
                    friendsUi = emptyList()
                }
            }
            
            // Load unread message counts (dot logic: dot visible if count > 0)
            LaunchedEffect(Unit) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    try {
                        chatRepo.getUnreadMessageCount(currentUserId).collect { counts ->
                            // Defensive: filter out zero or negative values
                            unreadCounts = counts.filterValues { it > 0 }
                            
                            // Update friendsUi to reflect unread status
                            friendsUi = friendsUi.map { friend ->
                                val hasUnread = unreadCounts.containsKey(friend.userId)
                                friend.copy(hasUnreadMessage = hasUnread)
                            }
                        }
                    } catch (e: Exception) {
                        println("Error loading unread counts: ${e.message}")
                    }
                }
            }

            // One-shot listener for unseen messages -> show local notification
            LaunchedEffect(Unit) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                if (currentUserId != null) {
                    val db = FirebaseDatabase.getInstance("https://sumup-31d9b-default-rtdb.asia-southeast1.firebasedatabase.app")
                    val seenStore = context.getSharedPreferences("seen_messages", android.content.Context.MODE_PRIVATE)
                    val ref = db.reference.child("messages")
                    val listener = object : com.google.firebase.database.ValueEventListener {
                        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                            for (child in snapshot.children) {
                                val msg = child.getValue(com.example.sumup.data.model.Message::class.java)
                                if (msg != null && msg.receiverId == currentUserId && !msg.isRead) {
                                    val key = msg.messageId
                                    if (!seenStore.getBoolean(key, false)) {
                                        // show notification only once
                                        val otherId = msg.senderId
                                        val sorted = listOf(currentUserId, otherId).sorted()
                                        val roomId = "${sorted[0]}_${sorted[1]}"
                                        com.example.sumup.util.NotificationHelper.showMessageNotification(
                                            context,
                                            senderName = friendsUi.firstOrNull { it.userId == otherId }?.name ?: "New message",
                                            messageContent = msg.content,
                                            chatRoomId = roomId,
                                            notificationId = key.hashCode()
                                        )
                                        seenStore.edit().putBoolean(key, true).apply()
                                    }
                                }
                            }
                        }
                        override fun onCancelled(error: com.google.firebase.database.DatabaseError) { }
                    }
                    ref.orderByChild("receiverId").equalTo(currentUserId).addValueEventListener(listener)
                }
            }

            ChatMainScreen(
                userName = userName, // Pass the current user's name
                onChatClick = { friendName ->
                    // Find the friend data to get the friendId
                    val friend = friendsUi.find { it.name == friendName }
                    if (friend != null) {
                        navController.navigate("${Routes.CHAT_DETAIL}/${friendName}/${friend.userId}")
                    }
                },
                onAddFriendClick = {
                    navController.navigate(Routes.ADD_FRIEND)
                },
                onNotificationClick = {
                    navController.navigate(Routes.FRIEND_REQUESTS)
                },
                friends = friendsUi,
                unreadCounts = unreadCounts,
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
        composable(Routes.ADD_FRIEND) {
            AddFriendScreen(
                onBack = { navController.popBackStack() },
                onAddFriend = { userId ->
                    // TODO: Implement actual friend adding logic here
                    // For now, just show a toast and go back
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.FRIEND_REQUESTS) {
            FriendRequestScreen(
                onBack = { navController.popBackStack() },
                onAcceptRequest = { userId ->
                    // TODO: Implement actual friend request acceptance logic here
                    // For now, just go back
                    navController.popBackStack()
                },
                onDeclineRequest = { userId ->
                    // TODO: Implement actual friend request decline logic here
                    // For now, just go back
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = "${Routes.CHAT_DETAIL}/{friendName}/{friendId}",
            arguments = listOf(
                navArgument("friendName") { type = NavType.StringType },
                navArgument("friendId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val friendName = backStackEntry.arguments?.getString("friendName") ?: ""
            val friendId = backStackEntry.arguments?.getString("friendId") ?: ""
            
            ChatDetailScreen(
                friendName = friendName,
                friendId = friendId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


