package com.example.sumup.presentation.screen.admin

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
fun AdminMainScreen(
    onManageClick: () -> Unit,
    onLogout: () -> Unit = {},
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
            painter = painterResource(id = R.drawable.adminmainscreenpic), // put your illustration in res/drawable
            contentDescription = "App Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Subtitle
        Text(
            text = "Welcome back boss",
            fontSize = 18.sp,
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
                onClick = { onManageClick() },
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = purpleMain
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("Manage User Accounts", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        TextButton(onClick = onLogout) {
            Text(
                text = "Log Out",
                color = Color.Red,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AdminMainScreen(
        onManageClick = {}
    )
}