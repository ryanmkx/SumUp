package com.example.sumup.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R

@Composable
fun MainScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val purpleMain = Color(0xFF7676FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Illustration (replace with your drawable)
        Image(
            painter = painterResource(id = R.drawable.loginmainscreenpic), // put your illustration in res/drawable
            contentDescription = "App Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        // Title
        Text(
            text = "Discover Our \n Summarizer Here",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = purpleMain,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Subtitle
        Text(
            text = "Explore our apps now",
            fontSize = 15.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(80.dp))
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment= Alignment.CenterVertically,
        ){
            // Login Button
            Button(
                onClick = { onLoginClick() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp)
            ) {
                Text("Login", fontSize = 16.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.width(30.dp))

            // Register (text only)
            Button(
                onClick = { onRegisterClick() },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .width(130.dp)
                    .height(50.dp)
            ) {
                Text("Sign Up", fontSize = 16.sp, color = purpleMain)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen(
        onLoginClick = {},
        onRegisterClick = {}
    )
}