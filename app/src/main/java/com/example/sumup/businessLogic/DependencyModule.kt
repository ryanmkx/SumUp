package com.example.sumup.businessLogic

import com.example.sumup.data.repository.*
import com.example.sumup.presentation.viewModel.ChatViewModel
import com.example.sumup.presentation.viewModel.HistoryViewModel
import com.example.sumup.presentation.viewModel.ProfileViewModel
import com.example.sumup.presentation.viewModel.AdminAccountsViewModel
import com.example.sumup.presentation.viewModel.FriendRequestViewModel
import com.example.sumup.presentation.viewModel.EditProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * Dependency Injection module for proper 3-layer architecture separation
 * This ensures that dependencies are created in the correct order and
 * each layer only depends on the layers below it.
 * 
 * Located in businessLogic package as it manages business logic components
 * (use cases and repositories) and provides them to the presentation layer.
 */
object DependencyModule {
    
    // Firebase instances (singletons)
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val firebaseDatabase: FirebaseDatabase by lazy { 
        FirebaseDatabase.getInstance("https://sumup-31d9b-default-rtdb.asia-southeast1.firebasedatabase.app")
    }
    private val firebaseStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    
    // Data Layer - Repositories
    val userAuthRepository: UserAuthRepository by lazy { 
        FirebaseAuthRepository(firebaseAuth) 
    }
    
    val userProfileRepository: UserProfileRepository by lazy { 
        FirestoreUserProfileRepository(firebaseFirestore) 
    }
    
    val summaryRepository: SummaryRepository by lazy { 
        FirestoreSummaryRepository(firebaseFirestore) 
    }
    
    val chatRepository: ChatRepository by lazy { 
        FirebaseChatRepository(firebaseDatabase) 
    }
    
    // Business Logic Layer - Use Cases
    val logInUseCase: LogIn by lazy { 
        LogIn(userAuthRepository, userProfileRepository) 
    }
    
    val signUpUseCase: SignUp by lazy { 
        SignUp(userAuthRepository, userProfileRepository) 
    }
    
    val updateProfileUseCase: UpdateProfile by lazy { 
        UpdateProfile(userAuthRepository, userProfileRepository) 
    }
    
    val getFriendRequestsUseCase: GetFriendRequests by lazy { 
        GetFriendRequests(userAuthRepository, userProfileRepository) 
    }
    
    val acceptFriendRequestUseCase: AcceptFriendRequest by lazy { 
        AcceptFriendRequest(userAuthRepository, userProfileRepository) 
    }
    
    val declineFriendRequestUseCase: DeclineFriendRequest by lazy { 
        DeclineFriendRequest(userAuthRepository, userProfileRepository) 
    }
    
    val getUserProfileUseCase: GetUserProfile by lazy { 
        GetUserProfile(userAuthRepository, userProfileRepository) 
    }
    
    val forgotPasswordUseCase: ForgotPassword by lazy { 
        ForgotPassword(userAuthRepository, userProfileRepository) 
    }
    
    val changePasswordUseCase: ChangePassword by lazy { 
        ChangePassword(userAuthRepository) 
    }
    
    val getUserSummariesUseCase: GetUserSummaries by lazy { 
        GetUserSummaries(userAuthRepository, summaryRepository) 
    }
    
    val sendMessageUseCase: SendMessage by lazy { 
        SendMessage(userAuthRepository, chatRepository) 
    }
    
    val getMessagesUseCase: GetMessages by lazy { 
        GetMessages(userAuthRepository, chatRepository) 
    }
    
    val createChatRoomUseCase: CreateChatRoom by lazy { 
        CreateChatRoom(userAuthRepository, chatRepository) 
    }
    
    val markMessageAsReadUseCase: MarkMessageAsRead by lazy { 
        MarkMessageAsRead(userAuthRepository, chatRepository) 
    }
    
    val getUnreadMessageCountUseCase: GetUnreadMessageCount by lazy { 
        GetUnreadMessageCount(userAuthRepository, chatRepository) 
    }
    
    // Admin use cases
    val listUsersByLevelUseCase: ListUsersByLevel by lazy {
        ListUsersByLevel(userProfileRepository)
    }
    val searchUsersUseCase: SearchUsers by lazy {
        SearchUsers(userProfileRepository)
    }
    val deleteUserAccountUseCase: DeleteUserAccount by lazy {
        DeleteUserAccount(userProfileRepository)
    }
    
    // Presentation Layer - ViewModels
    fun createProfileViewModel(): ProfileViewModel {
        return ProfileViewModel(userAuthRepository, userProfileRepository)
    }
    
    fun createHistoryViewModel(): HistoryViewModel {
        return HistoryViewModel(userAuthRepository, summaryRepository)
    }
    
    fun createChatViewModel(): ChatViewModel {
        return ChatViewModel(
            sendMessageUseCase,
            getMessagesUseCase,
            createChatRoomUseCase,
            markMessageAsReadUseCase,
            getUnreadMessageCountUseCase
        )
    }
    
    fun createFriendRequestViewModel(): FriendRequestViewModel {
        return FriendRequestViewModel(
            getFriendRequestsUseCase,
            acceptFriendRequestUseCase,
            declineFriendRequestUseCase
        )
    }
    
    fun createEditProfileViewModel(): EditProfileViewModel {
        return EditProfileViewModel(
            userProfileRepository,
            firebaseAuth,
            firebaseStorage
        )
    }
    
    fun createAdminAccountsViewModel(): AdminAccountsViewModel {
        return AdminAccountsViewModel(
            listUsersByLevelUseCase,
            searchUsersUseCase,
            deleteUserAccountUseCase
        )
    }
}
