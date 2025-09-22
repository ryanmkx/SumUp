package com.example.sumup.presentation.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.ui.lightPurpleMain
import com.example.sumup.presentation.screen.ui.purpleMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onLogin: (String, String) -> Unit = { _, _ -> },
    onCreateAccount: () -> Unit = {},
) {
    // State variables for text fields
    var logInEmail by remember { mutableStateOf("") }
    var logInPassword by remember { mutableStateOf("") }

    // Focus manager to dismiss keyboard
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Back Button
        IconButton(
            onClick = onBack,
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
                text = "Login",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = purpleMain // A shade of purple
            )

            Spacer(modifier = Modifier.height(15.dp))

            // "Welcome back..." text
            Text(
                text = "Welcome back you've been missed!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Email input field
            OutlinedTextField(
                value = logInEmail,
                onValueChange = { logInEmail = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
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
                value = logInPassword,
                onValueChange = {logInPassword = it},
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.White,
                    focusedLabelColor = purpleMain, // Label color when focused
                    unfocusedLabelColor = Color.Gray, // Label color when unfocused
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(10.dp))

            // "Forgot Password?" text
            TextButton(
                onClick = onForgotPassword,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    fontWeight = FontWeight.Bold,
                    color = purpleMain
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            // "Login" button
            Button(
                onClick = {
                    val email = logInEmail.trim()
                    val password = logInPassword
                    when {
                        email.isEmpty() && password.isEmpty() -> {
                            Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                            focusManager.clearFocus()
                        }
                        email.isEmpty() -> {
                            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                            focusManager.clearFocus()
                        }
                        password.isEmpty() -> {
                            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                            focusManager.clearFocus()
                        }
                        else -> onLogin(email, password)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "Login", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "Create a new account" text
            TextButton(
                onClick = onCreateAccount
            ) {
                Text(
                    text = "Create a new account",
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
fun LoginScreenPreview() {
    LoginScreen()
}