package com.example.sumup.presentation.screen.login


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.ui.lightpurpleMain
import com.example.sumup.presentation.screen.ui.purpleMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun SignUpScreen() {
    // State variables for text fields
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Focus manager to dismiss keyboard
    val focusManager = LocalFocusManager.current
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Back Button
        IconButton(
            onClick = { /* Handle back button click */ },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(60.dp))

            // "Login" title
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = purpleMain // A shade of purple
            )

            Spacer(modifier = Modifier.height(15.dp))

            // "Welcome back..." text
            Text(
                text = "Sign Up to save your summary record!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Email input field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = purpleMain, // Label color when focused
                    unfocusedLabelColor = Color.Gray, // Label color when unfocused
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = purpleMain, // Label color when focused
                    unfocusedLabelColor = Color.Gray, // Label color when unfocused
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = purpleMain, // Label color when focused
                    unfocusedLabelColor = Color.Gray, // Label color when unfocused
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password input field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = purpleMain, // Label color when focused
                    unfocusedLabelColor = Color.Gray, // Label color when unfocused
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1
            )


            Spacer(modifier = Modifier.height(120.dp))

            // "Sign Up" button
            Button(
                onClick = { /* Handle login click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)

            ) {
                Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "Already have an account" text
            TextButton(
                onClick = { /* Handle create account click */ }
            ) {
                Text(
                    text = "Already have an account",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = purpleMain
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}