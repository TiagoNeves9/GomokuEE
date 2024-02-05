package com.example.gomokuee.Screens.Components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LargeCustomButtonView(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    FilledTonalButton(
        enabled = enabled,
        shape = CircleShape,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(top = 10.dp, bottom = 15.dp)
            .size(size = 50.dp)
    ) {
        content()
    }
}