package com.example.gomokuee.Screens.Home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.CustomContainerView
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Utils.HOME_FONT_SIZE
import com.example.gomokuee.ui.theme.GomokuEETheme

private data class MenuOption(
    val onOptionSelected: () -> Unit = { },
    val imageVector: ImageVector,
    val text: String
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    error: Exception? = null,
    onPlayRequest: () -> Unit = { },
    onFavoritesRequest: () -> Unit = { },
    onDismissError: () -> Unit = { },
    navigation: NavigationHandlers = NavigationHandlers()
){
    val menu: List<MenuOption> = listOf(
        MenuOption(onPlayRequest, Icons.Default.PlayArrow, stringResource(id = R.string.play_option)),
        MenuOption(onFavoritesRequest, Icons.Default.Favorite, stringResource(id = R.string.favourites_option))
    )

    Scaffold(
        topBar = { CustomBar(text = stringResource(id = R.string.activity_home_title), navigation = navigation) }
    ) {
            padding ->
        CustomContainerView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val menuGroups = menu.chunked(2)

            for (option in menuGroups){
                Row(Modifier, Arrangement.Center) {
                    option.forEach { menuOption ->
                        MenuButton(menuOption.onOptionSelected) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = menuOption.imageVector,
                                    contentDescription = menuOption.text
                                )
                                Text(
                                    text = menuOption.text,
                                    fontSize = HOME_FONT_SIZE,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(onClick: () -> Unit = {}, content: @Composable () -> Unit) =
    ElevatedButton(
        shape = RoundedCornerShape(2.dp),
        onClick = onClick,
        modifier = Modifier
            .size(110.dp)
            .padding(2.dp)
    ) {
        content()
    }

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenPreview() =
    GomokuEETheme {
        HomeScreen()
    }