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
import com.example.sumup.presentation.screen.ui.lightpurpleMain
import com.example.sumup.presentation.screen.ui.purpleMain
import coil.compose.AsyncImage

@Composable
fun EditProfileScreen(
    initialName: String = "Elon Musk",
    initialEmail: String = "chingchong@gmail.com",
    initialPassword: String = "********************",
    onUpdate: (String, String, String) -> Unit = { _, _, _ -> },
    onNavigateBack: () -> Unit = {}
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf(initialPassword) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

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

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = {name = it},
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
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.Gray, // Label color when focused
                    unfocusedLabelColor = Color.Gray // Label color when unfocused
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email_icon),
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
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.Gray, // Label color when focused
                    unfocusedLabelColor = Color.Gray // Label color when unfocused
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password Field
            OutlinedTextField(
                value = initialPassword,
                onValueChange = {password = it},
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password_icon),
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
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = Color.Gray, // Label color when focused
                    unfocusedLabelColor = Color.Gray // Label color when unfocused
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(70.dp))

            // Update Button
            Button(
                onClick = {
                    Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                    onUpdate(name, email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp, max = 60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = purpleMain),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text(
                    text = "Update",
                    color = Color.White,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEditProfileScreen() {
    EditProfileScreen()
}
