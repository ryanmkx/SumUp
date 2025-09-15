package com.example.sumup.presentation.screen.common


import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sumup.presentation.screen.ui.purpleMain

@Composable
fun ConfirmationPopUp(
    showDialog: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Yes", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = purpleMain)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("No", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = purpleMain)
                }
            }
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ConfirmationPopUpPreview() {
    ConfirmationPopUp(
        message = "Your record would not be saved.",
        onConfirm = {},
        onDismiss = {},
        showDialog = true,
        title = "Are you sure?"
    )
}
