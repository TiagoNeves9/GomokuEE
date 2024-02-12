package com.example.gomokuee.Screens.Replay


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.CustomContainerView
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Game.DrawBoard
import com.example.gomokuee.Utils.BUTTON_DEFAULT_PADDING
import com.example.gomokuee.Utils.DEFAULT_CONTENT_PADDING
import com.example.gomokuee.Utils.DEFAULT_RADIUS
import com.example.gomokuee.Utils.ROW_DEFAULT_PADDING


data class ReplayHandlers(
    val onNext: (() -> Unit)? = null,
    val onPrev: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplayScreen(
    favInfo: FavInfo,
    navigation: NavigationHandlers = NavigationHandlers(),
    index: Int = 0,
    replayHandlers: ReplayHandlers = ReplayHandlers()
){
    Scaffold(
        topBar = { CustomBar(text = "Replay of ${favInfo.title}", navigation = navigation) }
    ) { padding ->
        CustomContainerView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ){
            Text(text = "Game versus ${favInfo.opponent}")
            val board = favInfo.plays[index]
            DrawBoard(board = board, selectedCell = null)

            NavigationButtons(replayHandlers, index,favInfo.plays.size)

            Text(text = "Game played at ${favInfo.date}")
        }

    }
}


@Composable
private fun NavigationButtons(replayHandlers: ReplayHandlers, index: Int, playsSize: Int){
    val modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = ROW_DEFAULT_PADDING, top = ROW_DEFAULT_PADDING)
    Row(modifier, Arrangement.Center) {
        if (index > 0) {
            replayHandlers.onPrev?.let { onPrev ->
                ReplayNavigationButton(Modifier, onPrev) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.prev)
                    )
                }

            }
        }
        if (index < playsSize - 1 ) {
            replayHandlers.onNext?.let { onNext ->
                ReplayNavigationButton(Modifier, onNext) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = stringResource(id = R.string.next)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplayNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { },
    content: @Composable () -> Unit = { }
) {
    Button(
        onClick = onClick,
        modifier = modifier.padding(BUTTON_DEFAULT_PADDING),
        shape = RoundedCornerShape(DEFAULT_RADIUS),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = Color.White
        ),
        contentPadding = PaddingValues(DEFAULT_CONTENT_PADDING)
    ) {
        content()
    }
}