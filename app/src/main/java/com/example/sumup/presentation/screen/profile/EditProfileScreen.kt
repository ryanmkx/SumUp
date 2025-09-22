package com.example.sumup.presentation.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.screen.ui.lightPurpleMain
import com.example.sumup.presentation.screen.ui.purpleMain
import coil.compose.AsyncImage
import android.util.Patterns

@Composable
fun EditProfileScreen(
    onUpdate: (String) -> Unit = { _ -> },
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Validation states
    var nameError by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    
    // Load user data when screen is first displayed
    LaunchedEffect(Unit) {
        try {
            // Import the necessary classes
            val authRepo = com.example.sumup.data.FirebaseAuthRepository(com.google.firebase.auth.FirebaseAuth.getInstance())
            val profileRepo = com.example.sumup.data.FirestoreUserProfileRepository(com.google.firebase.firestore.FirebaseFirestore.getInstance())
            
            val userId = authRepo.getCurrentUserId()
            if (userId != null) {
                val profileResult = profileRepo.getUserProfile(userId)
                if (profileResult.isSuccess) {
                    val user = profileResult.getOrNull()
                    name = user?.username ?: ""
                    println("DEBUG: Loaded username: '$name'")
                } else {
                    println("DEBUG: Failed to load profile: ${profileResult.exceptionOrNull()?.message}")
                }
            } else {
                println("DEBUG: No user ID found")
            }
        } catch (e: Exception) {
            println("DEBUG: Exception loading user data: ${e.message}")
        } finally {
            isLoading = false
        }
    }
    
    // Validation functions
    fun validateName(name: String): String {
        return when {
            name.isBlank() -> "Username is required"
            name.length < 3 -> "Username must be at least 3 characters"
            else -> ""
        }
    }
    
    // Check if form is valid
    val isFormValid = name.isNotBlank() && nameError.isEmpty()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
    }

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            HeaderWithBack(
                onNavigateBack = onNavigateBack,
                title = "Edit Profile"
            )
        },
//        bottomBar = {
//            FooterBar(
//                currentRoute = FooterNavigation.Profile,
//                onNavigate = {}
//            )
//        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = purpleMain
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Profile picture with edit button overlay
            Box(contentAlignment = Alignment.BottomEnd) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_pic), // your profile image
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(purpleMain, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_icon),
                        modifier = Modifier.size(24.dp),
                        contentDescription = "Edit Picture",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))

            // Username Field
            OutlinedTextField(
                value = name,
                onValueChange = { 
                    name = it
                    nameError = validateName(it)
                },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.profile_icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = purpleMain
                    )
                },
                modifier = Modifier
                    .width(330.dp)
                    .height(56.dp)
                ,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (nameError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (nameError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (nameError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (nameError.isNotEmpty()) Color.Red else Color.Gray
                ),
                isError = nameError.isNotEmpty(),
                supportingText = if (nameError.isNotEmpty()) {
                    { Text(nameError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Update Button
            Button(
                onClick = {
                    // Validate before updating
                    nameError = validateName(name)
                    
                    if (nameError.isEmpty()) {
                        Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        onUpdate(name)
                    } else {
                        Toast.makeText(context, "Please fix the errors before updating", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) purpleMain else Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(
                    text = "Update Profile",
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
fun PreviewEditProfileScreen() {
    EditProfileScreen()
}
