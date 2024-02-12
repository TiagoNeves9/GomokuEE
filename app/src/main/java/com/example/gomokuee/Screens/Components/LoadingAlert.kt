package com.example.gomokuee.Screens.Components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gomokuee.R
import com.example.gomokuee.ui.theme.DarkBlue50

@Composable
fun LoadingAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    onDismiss: () -> Unit = { },
    onDismissError: () -> Unit = { }
) {
    LoadingAlertImpl(
        stringResource(id = title),
        stringResource(id = message),
        onDismiss,
        onDismissError
    )
}

@Composable
fun LoadingAlertImpl(
    title: String,
    message: String,
    onDismiss: () -> Unit = { },
    onDismissError: () -> Unit = { }
) {
    AlertDialog(
        onDismissRequest = onDismissError,
        dismissButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.default_ack_button))
            }
        },
        confirmButton = { },
        title = { Text(text = title) },
        text = {
            CustomContainerView(
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(60.dp),
                    color = DarkBlue50,
                )
                Text(
                    text = message,
                    modifier = Modifier.padding(top = 25.dp)
                )
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Warning",
                modifier = Modifier.size(42.dp)
            )
        },
        modifier = Modifier
    )
}