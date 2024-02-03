package com.example.gomokuee.Screens.Components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

val CUSTOM_CONT_FONT_SIZE = 20.sp

@Composable
fun CustomContainerView(modifier: Modifier = Modifier, content: @Composable () -> Unit = {}) =
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        content()
    }
