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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.ui.theme.lightPurpleMain
import com.example.sumup.presentation.ui.theme.purpleMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.util.Patterns

@Composable
fun SignUpScreen(
    onBack: () -> Unit = {},
    onSignUp: (email: String, username: String, password: String) -> Unit = { _, _, _ -> },
    onAlreadyHaveAccount: () -> Unit = {},
) {
    // State variables for text fields
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    // Password visibility states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    
    // Validation states
    var emailError by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }
    
    // Focus manager to dismiss keyboard
    val focusManager = LocalFocusManager.current
    
    // Validation functions
    fun validateEmail(email: String): String {
        return when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email"
            else -> ""
        }
    }
    
    fun validateUsername(username: String): String {
        return when {
            username.isBlank() -> "Username is required"
            username.length < 3 -> "Username must be at least 3 characters"
            else -> ""
        }
    }
    
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
    
    // Check if all fields are valid and filled
    val isFormValid = email.isNotBlank() && username.isNotBlank() && 
                     password.isNotBlank() && confirmPassword.isNotBlank() &&
                     emailError.isEmpty() && usernameError.isEmpty() && 
                     passwordError.isEmpty() && confirmPasswordError.isEmpty()
    
    // Handle sign up button click
    fun handleSignUp() {
        // Validate all fields
        emailError = validateEmail(email)
        usernameError = validateUsername(username)
        passwordError = validatePassword(password)
        confirmPasswordError = validateConfirmPassword(password, confirmPassword)
        
        // If all validations pass, call onSignUp
        if (emailError.isEmpty() && usernameError.isEmpty() && 
            passwordError.isEmpty() && confirmPasswordError.isEmpty()) {
            onSignUp(email, username, password)
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
                onValueChange = { 
                    email = it
                    emailError = validateEmail(it)
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (emailError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (emailError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (emailError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (emailError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = emailError.isNotEmpty(),
                supportingText = if (emailError.isNotEmpty()) {
                    { Text(emailError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Username input field
            OutlinedTextField(
                value = username,
                onValueChange = { 
                    username = it
                    usernameError = validateUsername(it)
                },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (usernameError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (usernameError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (usernameError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (usernameError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = usernameError.isNotEmpty(),
                supportingText = if (usernameError.isNotEmpty()) {
                    { Text(usernameError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Password input field
            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    passwordError = validatePassword(it)
                    // Also validate confirm password when password changes
                    confirmPasswordError = validateConfirmPassword(it, confirmPassword)
                },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.clearFocus() }
                ),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightPurpleMain,
                    unfocusedContainerColor = lightPurpleMain,
                    focusedBorderColor = if (passwordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedBorderColor = if (passwordError.isNotEmpty()) Color.Red else Color.White,
                    focusedLabelColor = if (passwordError.isNotEmpty()) Color.Red else purpleMain,
                    unfocusedLabelColor = if (passwordError.isNotEmpty()) Color.Red else Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                isError = passwordError.isNotEmpty(),
                supportingText = if (passwordError.isNotEmpty()) {
                    { Text(passwordError, color = Color.Red, fontSize = 12.sp) }
                } else null,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password input field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { 
                    confirmPassword = it
                    confirmPasswordError = validateConfirmPassword(password, it)
                },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
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


            Spacer(modifier = Modifier.height(120.dp))

            // "Sign Up" button
            Button(
                onClick = { handleSignUp() },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) purpleMain else Color.Gray,
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)

            ) {
                Text(text = "Sign Up", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "Already have an account" text
            TextButton(
                onClick = onAlreadyHaveAccount
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