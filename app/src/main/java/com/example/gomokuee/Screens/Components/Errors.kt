package com.example.gomokuee.Screens.Components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gomokuee.R
import com.example.gomokuee.Service.ApiUnauthorizedException


abstract class InputException : Exception()

object EmptyTitle : InputException()
object EmptyOpponent : InputException()



@Composable
fun ProcessError(dismissError: () -> Unit, cause: Throwable) {
    //Pair with title and message
    val errorDetails: Pair<Int, Int> = when(cause) {
        is EmptyTitle -> Pair(R.string.error_invalid_title, R.string.title_is_blank_input_error)
        is EmptyOpponent -> Pair(R.string.error_invalid_opponent, R.string.opponent_is_blank_input_error)
        is ApiUnauthorizedException -> Pair(R.string.error_general_title, R.string.error_bad_login)
        else -> Pair(R.string.error_api_title, R.string.error_could_not_reach_api)
    }

    ErrorAlert(
        title = errorDetails.first,
        message = errorDetails.second,
        buttonText = R.string.error_retry_button_text,
        onDismiss = dismissError
    )
}

@Composable
fun ErrorAlert(
    @StringRes title: Int,
    @StringRes message: Int,
    @StringRes buttonText: Int,
    onDismiss: () -> Unit = { }
) {
    ErrorAlertImpl(
        title = stringResource(id = title),
        message = stringResource(id = message),
        buttonText = stringResource(id = buttonText),
        onDismiss = onDismiss
    )
}

@Composable
private fun ErrorAlertImpl(
    title: String,
    message: String,
    buttonText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        confirmButton = {
            OutlinedButton(
                border = BorderStroke(0.dp, Color.Unspecified),
                onClick = onDismiss
            ) {
                Text(text = buttonText)
            }
        },
        title = { Text(text = title) },
        text = { Text(text = message) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = "Warning"
            )
        },
    )
}
