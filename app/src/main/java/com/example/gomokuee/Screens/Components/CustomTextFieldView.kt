package com.example.gomokuee.Screens.Components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.gomokuee.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTitleTextFieldView(
    value: String = "",
    isError: Boolean = false,
    errorText: String = "",
    label : String,
    onClick: (String) -> Unit = {}
) =
    OutlinedTextField(
        value = value,
        onValueChange = onClick,
        label = { Text(text = label) },
        singleLine = true,
        isError = isError,
        supportingText = {
            SupportingText(isError = isError, errorText = errorText)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Games,
                contentDescription = stringResource(R.string.new_favourite_game_title)
            )
        },
        trailingIcon = { TrailingIcon(isError = isError) }
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOpponentTextFieldView(
    value: String = "",
    isError: Boolean = false,
    errorText: String = "",
    label : String,
    onClick: (String) -> Unit = {}
) =
    OutlinedTextField(
        value = value,
        onValueChange = onClick,
        label = { Text(text = label) },
        singleLine = true,
        isError = isError,
        supportingText = {
            SupportingText(isError = isError, errorText = errorText)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(R.string.new_favourite_game_opponent)
            )
        },
        trailingIcon = { TrailingIcon(isError = isError) }
    )
























@Composable
private fun SupportingText(isError: Boolean, errorText: String) {
    if (isError)
        Text(text = errorText, color = MaterialTheme.colorScheme.error)
}

@Composable
private fun TrailingIcon(isError: Boolean) {
    if (isError)
        Icon(
            Icons.Filled.Error, "error",
            tint = MaterialTheme.colorScheme.error
        )
}