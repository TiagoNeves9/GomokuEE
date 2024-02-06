package com.example.gomokuee.Screens.Game

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.gomokuee.Domain.Board.BOARD_CELL_SIZE
import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.Board.BoardDraw
import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Domain.Board.BoardWin
import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.Board.indexToColumn
import com.example.gomokuee.Domain.Board.indexToRow
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.Loading
import com.example.gomokuee.Domain.Turn
import com.example.gomokuee.Domain.exceptionOrNull
import com.example.gomokuee.Domain.getOrNull
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.CustomContainerView
import com.example.gomokuee.Screens.Components.LoadingAlert
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Components.ProcessError
import com.example.gomokuee.Service.GomokuGames
import com.example.gomokuee.Utils.BOARD_PLUS_SYMBOL_FULL_OFFSET
import com.example.gomokuee.Utils.BOARD_PLUS_SYMBOL_HALF_OFFSET
import com.example.gomokuee.Utils.BOARD_PLUS_SYMBOL_STROKE_WIDTH
import com.example.gomokuee.Utils.BOARD_PLUS_SYMBOL_ZERO_OFFSET
import com.example.gomokuee.ui.theme.GomokuEETheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    currentGame: LoadState<Game?>,
    selectedCell: Cell?,
    onCellSelected: (Cell) -> Unit = { },
    currentUser: String? = null,
    onPlayRequested: () -> Unit = { },
    onDismissError: () -> Unit = { },
    navigation: NavigationHandlers = NavigationHandlers()
){
    Scaffold(
        topBar = { CustomBar(text = stringResource(id = R.string.activity_game_title), navigation = navigation) }
    ) {
        padding ->
        CustomContainerView(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)

        ){
            currentGame.getOrNull()?.let { game ->

                Text(text = "${game.users.first.username} VS ${game.users.second.username}")

                DrawBoard(game.board, selectedCell) { cell ->
                   // if (game.currentPlayer.first.username == currentUser) {
                        onCellSelected(cell)
                    //}
                }

                if (selectedCell != null) {
                    OutlinedButton(onClick = onPlayRequested) {
                        Text(stringResource(id = R.string.make_move_button))
                    }
                }

                Spacer(Modifier.padding(vertical = BOARD_CELL_SIZE.dp))

                StatusBar {
                    when (game.board) {
                        is BoardRun -> {
                            Text(stringResource(id = R.string.turn_text).plus(" ${game.currentPlayer.first.username}"))
                            DrawTurnOrWinnerPiece(game.board)
                        }

                        is BoardWin -> {
                            val message = stringResource(id = R.string.game_winner_message).plus(" ${game.currentPlayer.first.username}")
                            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
                            DrawTurnOrWinnerPiece(game.board)
                        }

                        is BoardDraw -> {
                            val message = stringResource(id = R.string.game_draw)
                            Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            if (currentGame is Loading)
                LoadingAlert(R.string.loading_game_title, R.string.loading_game_message)

            currentGame.exceptionOrNull()?.let { cause ->
                ProcessError(onDismissError, cause)
            }
        }
    }

}

@Composable
fun DrawBoard(board: Board, selectedCell: Cell?, onClick: (Cell) -> Unit = {}) {
    SymbolAxisView(board.boardSize)

    Row(
        Modifier,
        Arrangement.Center,
        Alignment.Bottom
    ) {
        NumberAxisView(board.boardSize)
        CellsView(board, selectedCell, onClick)
        NumberAxisView(board.boardSize)
    }

    SymbolAxisView(board.boardSize)
}

@Composable
fun SymbolAxisView(boardSize: Int) {
    Row(
        Modifier.padding(top = 10.dp, bottom = 10.dp),
        Arrangement.SpaceEvenly
    ) {
        Box(Modifier.size(BOARD_CELL_SIZE.dp)) {
            AxisText(" ")
        }

        repeat(boardSize) {
            val letter = it.indexToColumn(boardSize).symbol.toString()
            Box(
                Modifier.size(BOARD_CELL_SIZE.dp),
                Alignment.Center
            ) {
                AxisText(letter)
            }
        }

        Box(Modifier.size(BOARD_CELL_SIZE.dp)) {
            AxisText(" ")
        }
    }
}

@Composable
fun NumberAxisView(boardSize: Int) =
    Column(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        repeat(boardSize) {
            val number = it.indexToRow(boardSize).number.toString()
            Box(
                Modifier.size(BOARD_CELL_SIZE.dp),
                Alignment.Center
            ) {
                AxisText(number)
            }
        }
    }

@Composable
fun AxisText(text: String) = Text(
    text = text,
    color = Color.Black,
    fontWeight = FontWeight.Bold,
    textAlign = TextAlign.Center
)

@Composable
fun CellsView(board: Board, selectedCell: Cell?, onClick: (Cell) -> Unit = {}) {
    Column {
        repeat(board.boardSize) { line ->
            Row {
                repeat(board.boardSize) { col ->
                    val cell = Cell(line, col, board.boardSize)
                    when (board.positions[cell]) {
                        Turn.BLACK_PIECE -> DrawCells(cell = cell, enabled = false) {
                            DrawBlackPiece()
                        }

                        Turn.WHITE_PIECE -> DrawCells(cell = cell, enabled = false) {
                            DrawWhitePiece()
                        }

                        else -> DrawCells(
                            onClick = onClick,
                            selectedCell = selectedCell,
                            cell = cell,
                            enabled = true
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawCells(
    onClick: (Cell) -> Unit = {},
    selectedCell: Cell? = null,
    cell: Cell,
    enabled: Boolean,
    content: @Composable () -> Unit = {}
) {
    val padding = (BOARD_CELL_SIZE / 2).dp

    val color = if (selectedCell == cell) Color.Red else Color.White

    val modifier = Modifier
        .size(BOARD_CELL_SIZE.dp)
        .background(color = color)
        .clickable(enabled = enabled) {
            onClick(cell)
        }

    Box(modifier, Alignment.Center) {
        DrawPlusSymbol(padding)
        content()
    }
}

@Composable
fun DrawPlusSymbol(padding: Dp) {
    val modifier = Modifier
        .fillMaxSize()
        .padding(padding)

    Canvas(modifier) {
        drawLine(
            color = Color.LightGray,
            start = Offset(BOARD_PLUS_SYMBOL_HALF_OFFSET, BOARD_PLUS_SYMBOL_ZERO_OFFSET),
            end = Offset(BOARD_PLUS_SYMBOL_HALF_OFFSET, BOARD_PLUS_SYMBOL_FULL_OFFSET),
            strokeWidth = BOARD_PLUS_SYMBOL_STROKE_WIDTH
        )
        drawLine(
            color = Color.LightGray,
            start = Offset(BOARD_PLUS_SYMBOL_ZERO_OFFSET, BOARD_PLUS_SYMBOL_HALF_OFFSET),
            end = Offset(BOARD_PLUS_SYMBOL_FULL_OFFSET, BOARD_PLUS_SYMBOL_HALF_OFFSET),
            strokeWidth = BOARD_PLUS_SYMBOL_STROKE_WIDTH
        )
    }
}

@Composable
fun DrawBlackPiece() = Image(painter = painterResource(id = R.drawable.b), contentDescription = null)

@Composable
fun DrawWhitePiece() = Image(painter = painterResource(id = R.drawable.w), contentDescription = null)

@Composable
fun DrawTurnOrWinnerPiece(board: Board) =
    if (board is BoardRun)
        if (board.turn == Turn.BLACK_PIECE) DrawBlackPiece()
        else DrawWhitePiece()
    else if (board is BoardWin)
        if (board.winner.second == Turn.BLACK_PIECE) DrawBlackPiece()
        else DrawWhitePiece()
    else throw IllegalStateException("Game is running or finished with a winner.")

@Composable
fun StatusBar(content: @Composable () -> Unit = {}) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
    Text(stringResource(id = R.string.activity_main_footer), color = Color.Black)
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    GomokuEETheme {
        val game = GomokuGames.games.first()
        GameScreen(loaded(Result.success(game)), null, {}, "jp")
    }
}