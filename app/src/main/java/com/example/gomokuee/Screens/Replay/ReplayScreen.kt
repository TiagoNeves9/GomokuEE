package com.example.gomokuee.Screens.Replay

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import com.example.gomokuee.Domain.Board.BOARD_DIM
import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.Board.createBoard
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Turn
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.CustomContainerView
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Game.DrawBoard
import com.example.gomokuee.Screens.Game.DrawTurnOrWinnerPiece
import com.example.gomokuee.Utils.BUTTON_DEFAULT_PADDING
import com.example.gomokuee.Utils.DEFAULT_CONTENT_PADDING
import com.example.gomokuee.Utils.DEFAULT_RADIUS
import com.example.gomokuee.Utils.ROW_DEFAULT_PADDING
import com.example.gomokuee.ui.theme.GomokuEETheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.days

data class ReplayHandlers(
    val onNext: (() -> Unit)? = null,
    val onPrev: (() -> Unit)? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplayScreen(
    plays : List<BoardRun>,
    navigation: NavigationHandlers = NavigationHandlers(),
    index: Int = 0,
    replayHandlers: ReplayHandlers = ReplayHandlers()
){
    Scaffold(
        topBar = { CustomBar(text = "Replay of TEST", navigation = navigation) }
    ) { padding ->
        CustomContainerView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ){
            Text(text = "Game versus TEST")

            DrawBoard(board = BoardRun(plays[index].positions, plays[index].turn, BOARD_DIM), selectedCell = null)

            NavigationButtons(replayHandlers)

            Text(text = "Game played at TEST")
        }

    }
}


@Composable
private fun NavigationButtons(replayHandlers: ReplayHandlers){
    val modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = ROW_DEFAULT_PADDING, top = ROW_DEFAULT_PADDING)
    Row(modifier, Arrangement.Center) {
        replayHandlers.onPrev?.let {onPrev ->
            ReplayNavigationButton(Modifier, onPrev) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.prev))
            }

        }
        replayHandlers.onNext?.let { onNext ->
            ReplayNavigationButton(Modifier, onNext) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = stringResource(id = R.string.next))
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReplayPreview(){
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
    val current = LocalDateTime.now().format(formatter).toString()
    val cell1: Cell = Cell.invoke(11, 4, BOARD_DIM)
    val cell2: Cell = Cell.invoke(10, 6, BOARD_DIM)
    val plays : List<BoardRun> = listOf(
        BoardRun(mapOf(Pair(cell1,Turn.BLACK_PIECE)),Turn.BLACK_PIECE, BOARD_DIM),
        BoardRun(mapOf(Pair(cell2,Turn.WHITE_PIECE)),Turn.WHITE_PIECE, BOARD_DIM),
        )
    GomokuEETheme {
        ReplayScreen(
            plays,
            index = 0,
            replayHandlers = ReplayHandlers( onNext = { }, onPrev = { })
        )
    }
}