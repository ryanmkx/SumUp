package com.example.sumup.presentation.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.ui.lightpurpleMain
import com.example.sumup.presentation.screen.ui.purpleMain
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Back Button
        IconButton(
            onClick = { /* Handle back button click */ },
            modifier = Modifier.align(Alignment.TopStart)
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
                value = "",
                onValueChange = {},
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray, // Label color when focused
                    unfocusedLabelColor = Color.Gray // Label color when unfocused
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password input field
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = lightpurpleMain,
                    unfocusedContainerColor = lightpurpleMain,
                    focusedBorderColor = purpleMain,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.Gray, // Label color when focused
                    unfocusedLabelColor = Color.Gray // Label color when unfocused
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // "Forgot Password?" text
            TextButton(
                onClick = { /* Handle forgot password click */ },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Forgot Password?",
                    fontWeight = FontWeight.Medium,
                    color = purpleMain
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            // "Login" button
            Button(
                onClick = { /* Handle login click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Login", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // "Create a new account" text
            TextButton(
                onClick = { /* Handle create account click */ }
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