package com.example.sumup.presentation.screen.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.ui.theme.lightPurpleMain
import com.example.sumup.presentation.ui.theme.purpleMain

fun validateEmail(email: String): String {
    return when {
        email.isBlank() -> "Email is required"
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Please enter a valid email"
        else -> ""
    }
}

@Composable
fun ForgotPasswordEmailScreen(
    onSendEmail: (String) -> Unit = {},
    onBack: () -> Unit = {},
    isLoading: Boolean = false,
) {
    // State variables for text fields
    var email by remember { mutableStateOf("") }
    val isValid = remember(email) { email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() }
    var emailError by remember { mutableStateOf("") }

    // Focus manager to dismiss keyboard
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    
    // Function to handle sending email
    fun handleSendEmail() {
        if (isValid) {
            onSendEmail(email)
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

            Spacer(modifier = Modifier.height(130.dp))

            // "Forgot Password" title
            Text(
                text = "Forgot Password?",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = purpleMain // A shade of purple
            )

            Spacer(modifier = Modifier.height(15.dp))

            // "Enter a email and we will send a recovery code." text
            Text(
                text = "Enter a email and we will send a recovery code.",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.height(50.dp))


            // Next button
            Button(
                onClick = { handleSendEmail() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                enabled = isValid && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Next",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun ForgotPasswordEmailScreenPreview() {
    ForgotPasswordEmailScreen()
}