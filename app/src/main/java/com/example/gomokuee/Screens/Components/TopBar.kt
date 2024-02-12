package com.example.gomokuee.Screens.Components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gomokuee.R

data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onFavouriteRequested: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    text: String,
    navigation: NavigationHandlers = NavigationHandlers()
) =
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(size = 10.dp)),
        navigationIcon = {
            if (navigation.onBackRequested != null)
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.top_bar_go_back)
                    )
                }
        },
    )

@Composable
fun CustomBar(text: String, navigation: NavigationHandlers = NavigationHandlers()) =
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        TopBar(text, navigation)
    }
