package com.example.sumup.presentation.screen.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.ui.theme.lightPurpleMain
import com.example.sumup.presentation.ui.theme.purpleMain

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit = {},
    onPasswordChanged: (currentPassword: String, newPassword: String) -> Unit = { _, _ -> },
) {
    // State variables for text fields
    val focusManager = LocalFocusManager.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var currentPasswordError by remember { mutableStateOf("") }
    var newPasswordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Password visibility states
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    fun validatePassword(password: String): String {
        return when {
            password.isBlank() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            !password.any { it.isDigit() } -> "Password must contain at least 1 digit"
            !password.any { "!@#$%^&*()_+-=[]{}|;:,.<>?".contains(it) } -> "Password must contain at least 1 symbol"
            else -> ""
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String {
        return when {
            confirmPassword.isBlank() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> ""
        }
    }

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(130.dp))

            // "Change Password" title
            Text(
                text = "Change Password",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = purpleMain
            )

            Spacer(modifier = Modifier.height(15.dp))

            // "Enter your current password and set a new password" text
            Text(
                text = "Enter your current password and set a new password.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Current password input field
            OutlinedTextField(
                value = currentPassword,
                onValueChange = {
                    currentPassword = it
                    currentPasswordError = ""
                },
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                        Icon(
                            imageVector = if (currentPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (currentPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (currentPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (currentPasswordError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (currentPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (currentPasswordError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = currentPasswordError.isNotEmpty(),
                supportingText = if (currentPasswordError.isNotEmpty()) {
                    { Text(currentPasswordError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // New password input field
            OutlinedTextField(
                value = newPassword,
                onValueChange = {
                    newPassword = it
                    newPasswordError = validatePassword(it)
                    // Also validate confirm password when new password changes
                    confirmPasswordError = validateConfirmPassword(it, confirmPassword)
                },
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                        Icon(
                            imageVector = if (newPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (newPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (newPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (newPasswordError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (newPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (newPasswordError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = newPasswordError.isNotEmpty(),
                supportingText = if (newPasswordError.isNotEmpty()) {
                    { Text(newPasswordError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm password input field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = validateConfirmPassword(newPassword, it)
                },
                label = { Text("Confirm New Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (confirmPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (confirmPasswordError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (confirmPasswordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (confirmPasswordError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = confirmPasswordError.isNotEmpty(),
                supportingText = if (confirmPasswordError.isNotEmpty()) {
                    { Text(confirmPasswordError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(50.dp))

            // Change password button
            Button(
                onClick = { 
                    val currentValidation = if (currentPassword.isBlank()) "Current password is required" else ""
                    val newValidation = validatePassword(newPassword)
                    val confirmValidation = validateConfirmPassword(newPassword, confirmPassword)
                    
                    currentPasswordError = currentValidation
                    
                    if (currentValidation.isEmpty() && newValidation.isEmpty() && confirmValidation.isEmpty()) {
                        isLoading = true
                        onPasswordChanged(currentPassword, newPassword)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                enabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && 
                         confirmPassword.isNotBlank() && currentPasswordError.isEmpty() &&
                         newPasswordError.isEmpty() && confirmPasswordError.isEmpty() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Change Password",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChangePasswordScreenPreview() {
    ChangePasswordScreen()
}
