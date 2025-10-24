package com.example.sumup.presentation.screen.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.common.HeaderWithBack
import com.example.sumup.presentation.ui.theme.lightPurpleMain
import com.example.sumup.presentation.ui.theme.purpleMain
import coil.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sumup.businessLogic.DependencyModule
import com.example.sumup.presentation.viewModel.EditProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onUpdate: (String) -> Unit = { _ -> },
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedStatus by remember { mutableStateOf("active") }
    var expanded by remember { mutableStateOf(false) }
    
    // Validation states
    var nameError by remember { mutableStateOf("") }
    
    val context = LocalContext.current
    
    // Use ViewModel instead of direct repository access - follows 3-layer architecture
    val viewModel: EditProfileViewModel = viewModel { DependencyModule.createEditProfileViewModel() }
    
    // Collect state from ViewModel
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val isUploadingImage by viewModel.isUploadingImage.collectAsStateWithLifecycle()
    
    // Load user data when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }
    
    // Update name and status when profile loads
    LaunchedEffect(userProfile) {
        userProfile?.let { profile ->
            name = profile.username
            selectedStatus = profile.status
        }
    }
    
    // Handle error messages
    LaunchedEffect(error) {
        error?.let { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    
    // Clear selectedImageUri after successful upload and show success message
    LaunchedEffect(userProfile) {
        if (userProfile?.profilePic?.isNotEmpty() == true && selectedImageUri != null) {
            selectedImageUri = null
            Toast.makeText(context, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show()
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
        uri?.let { imageUri ->
            selectedImageUri = imageUri
            // Upload image to Firebase Storage
            viewModel.uploadProfileImage(imageUri)
        }
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
                when {
                    selectedImageUri != null -> {
                        // Show selected image (before upload)
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    userProfile?.profilePic?.isNotEmpty() == true -> {
                        // Show uploaded image from Firebase Storage
                        val profilePic = userProfile?.profilePic ?: ""
                        AsyncImage(
                            model = profilePic,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        // Show default image
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(140.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                IconButton(
                    onClick = {
                        if (!isUploadingImage) {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(if (isUploadingImage) Color.Gray else purpleMain, CircleShape),
                    enabled = !isUploadingImage
                ) {
                    if (isUploadingImage) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.edit_icon),
                            modifier = Modifier.size(24.dp),
                            contentDescription = "Edit Picture",
                            tint = Color.White
                        )
                    }
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
                    .height(65.dp)
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

            Spacer(modifier = Modifier.height(20.dp))

            // Account Status Dropdown
            Text(
                text = "Account Status",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = purpleMain,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.width(330.dp)
            ) {
                OutlinedTextField(
                    value = selectedStatus.replaceFirstChar { it.uppercase() },
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .width(330.dp)
                        .height(65.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightPurpleMain,
                        unfocusedContainerColor = lightPurpleMain,
                        focusedBorderColor = purpleMain,
                        unfocusedBorderColor = Color.White,
                        focusedLabelColor = purpleMain,
                        unfocusedLabelColor = Color.Gray
                    ),
                    label = { Text("Select Status") }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Active") },
                        onClick = {
                            selectedStatus = "active"
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sleep") },
                        onClick = {
                            selectedStatus = "sleep"
                            expanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Status Description
            Text(
                text = when (selectedStatus) {
                    "active" -> "Others can add you as a friend"
                    "sleep" -> "Others cannot add you as a friend"
                    else -> ""
                },
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            Spacer(modifier = Modifier.height(60.dp))

            // Update Button
            Button(
                onClick = {
                    // Validate before updating
                    nameError = validateName(name)
                    
                    if (nameError.isEmpty()) {
                        // Use ViewModel to update profile - follows 3-layer architecture
                        viewModel.updateProfile(name, selectedStatus)
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
