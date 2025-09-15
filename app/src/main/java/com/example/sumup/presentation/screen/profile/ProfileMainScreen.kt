package com.example.sumup.presentation.screen.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.R
import com.example.sumup.presentation.screen.ui.purpleMain
import com.example.sumup.presentation.screen.common.FooterBar
import com.example.sumup.presentation.screen.common.FooterNavigation
import com.example.sumup.presentation.screen.common.Header

@Composable
fun ProfileMainScreen(
    userName: String = "ee",
    email: String = "chingchong@gmail.com",
    passwordMasked: String = "********",
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Scaffold(
        topBar = { Header(title = "Profile") },
        bottomBar = { FooterBar(currentRoute = FooterNavigation.Profile, onNavigate = {}) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // ======= Main scrollable content =======
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Profile Picture
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(25.dp))

                // User Name
                Text(
                    text = userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Email Box
                ProfileInfoBox(
                    icon = R.drawable.email_icon,
                    value = email
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Box
                ProfileInfoBox(
                    icon = R.drawable.password_icon,
                    value = passwordMasked
                )
            }

            // ======= Buttons anchored at the bottom =======
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp) // distance from screen bottom
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onEditProfile,
                    modifier = Modifier
                        .width(330.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C4DFF) // Purple
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text(
                        text = "Edit Profile",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
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
    }
}


@Composable
fun ProfileInfoBox(icon: Int, value: String) {
    Row(
        modifier = Modifier
            .width(330.dp)
            .height(56.dp)
            .background(Color(0xFFEDE7F6),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = purpleMain,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = purpleMain,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileMainScreen(

    )
}
