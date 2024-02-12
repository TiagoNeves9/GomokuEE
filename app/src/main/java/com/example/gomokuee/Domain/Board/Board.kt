package com.example.gomokuee.Domain.Board

import android.util.Log
import com.example.gomokuee.Domain.Exceptions
import com.example.gomokuee.Domain.Player
import com.example.gomokuee.Domain.Turn



const val BOARD_DIM = 15
const val BIG_BOARD_DIM = 19
const val N_ON_ROW = 5
const val BOARD_CELL_SIZE = 21

const val BOARD_RUNNING = "RUNNING"
const val BOARD_DRAW = "DRAW"
const val BOARD_BLACK_WON = "BLACK_WON"
const val BOARD_WHITE_WON = "WHITE_WON"


sealed class Board(val positions: Map<Cell, Turn>, val boardSize: Int) {
    init {
        check(boardSize >= N_ON_ROW) { "Board dimension must be >= to $N_ON_ROW" }
    }

    fun addPiece(cell: Cell): BoardRun {
        check(this is BoardRun) { "Game finished." }

        return if (cell.rowIndex !in 0 until boardSize || cell.colIndex !in 0 until boardSize)
            throw Exceptions.PlayNotAllowedException("Invalid cell (outside of the board dimensions)!")
        else if (cell.toString() in this.positions.map { it.key.toString() })
            throw Exceptions.WrongPlayException("Square already occupied!")
        else {
            val newMap: Map<Cell, Turn> = this.positions + mapOf(cell to this.turn)
            BoardRun(newMap, this.turn.other(), boardSize)
        }
    }

    fun positionsToString(): String {
        var str = ""
        positions.forEach {
            val key = if (it.key.rowIndex < 9) "0${it.key}" else "${it.key}"
            str += if (it.value == Turn.BLACK_PIECE) "${key}B"
            else "${key}W"
        }
        return str
    }

    fun typeToString(): String = when (this) {
        is BoardRun -> BOARD_RUNNING
        is BoardDraw -> BOARD_DRAW
        is BoardWin -> {
            if (winner.second == Turn.BLACK_PIECE) BOARD_BLACK_WON
            else BOARD_WHITE_WON
        }
    }

    fun serialize(): String {
        val klassName = this.typeToString()
        val boardDim = boardSize.boardSizeString()
        val moves = positionsToString()
        return "$klassName+$boardDim+$moves"
    }

}



fun String.stringToPositions(boardSize: Int): Map<Cell, Turn> {
    Log.v("StringCheck", this)
    Log.v("StringSize", this.length.toString())
    check(this.length % 4 == 0) { "Invalid string length." }
    val map = mutableMapOf<Cell, Turn>()
    var i = 0
    while (i + 3 < this.length) {
        val row = (this[i].toString() + this[i + 1].toString())
        val col = this[i + 2].toString()
        val cell = (row + col).toCell(boardSize)
        val piece = if (this[i + 3] == 'B') Turn.BLACK_PIECE else Turn.WHITE_PIECE
        map += mapOf(cell to piece)
        i += 4  //each cell-piece pair is represented by 4 chars
    }
    return map
}

fun String.stringToType(lastBoard: Board, lastPlayer: Player): Board = when (this) {
    BOARD_RUNNING -> BoardRun(lastBoard.positions, lastPlayer.second, lastBoard.boardSize)
    BOARD_DRAW -> BoardDraw(lastBoard.positions, lastBoard.boardSize)
    BOARD_BLACK_WON -> BoardWin(lastBoard.positions, lastPlayer, lastBoard.boardSize)
    BOARD_WHITE_WON -> BoardWin(lastBoard.positions, lastPlayer, lastBoard.boardSize)
    else -> throw IllegalArgumentException("Invalid board type.")
}

fun Map<String, Turn>.stringToCell(boardDim: Int): Map<Cell, Turn> = mapKeys {
        it.key.toCell(boardDim)
    }


class BoardRun(positions: Map<Cell, Turn>, val turn: Turn, boardSize: Int) : Board(positions, boardSize) {
    fun checkWin(lastMove: Cell): Boolean =
        positions.size >= 2 * N_ON_ROW - 1 &&
                (checkWinInDir(lastMove, Direction.UP, Direction.DOWN, boardSize) ||
                        checkWinInDir(lastMove, Direction.LEFT, Direction.RIGHT, boardSize) ||
                        checkWinInDir(lastMove, Direction.UP_LEFT, Direction.DOWN_RIGHT, boardSize) ||
                        checkWinInDir(lastMove, Direction.UP_RIGHT, Direction.DOWN_LEFT, boardSize))

    fun checkDraw(boardSize: Int): Boolean = positions.size == boardSize * boardSize

    private fun checkWinInDir(lastMove: Cell, dir1: Direction, dir2: Direction, boardSize: Int): Boolean {
        val line = cellsInDir(lastMove, dir1, boardSize).reversed() +
                lastMove + cellsInDir(lastMove, dir2, boardSize)
        return checkWinInLine(line)
    }

    private fun checkWinInLine(line: List<Cell>): Boolean {
        var count = 0
        for (cell in line) {
            if (this.positions[cell] == this.turn.other()) {
                count++
                if (count >= N_ON_ROW)
                    return true
            } else count = 0
        }
        return false
    }






}
fun String.deserializeToBoard(): Board{
    val words = this.split("+")
    val board = words[0]
    val boardDim = words[1].toBoardDim()
    val moves = if (words[2].isBlank()) emptyMap<Cell,Turn>() else words[2].stringToPositions(boardDim)
    if (moves.isEmpty()) return createBoard(boardSize = boardDim)
    return when(board){
        BOARD_RUNNING -> BoardRun(moves,moves.toList().last().second,boardDim)
        BOARD_DRAW -> BoardDraw(moves,boardDim)
        else -> throw IllegalStateException("There is no board type for $board")
    }
}


class BoardWin(positions: Map<Cell, Turn>, val winner: Player, boardSize: Int) : Board(positions, boardSize)

class BoardDraw(positions: Map<Cell, Turn>, boardSize: Int) : Board(positions, boardSize)

fun createBoard(firstTurn: Turn = Turn.BLACK_PIECE, boardSize: Int) = BoardRun(mapOf(), firstTurn, boardSize)

fun Int.boardSizeString(): String = if (this == BOARD_DIM) "15x15" else "19x19"

fun String.toBoardDim() : Int = if (this == "15x15") BOARD_DIM else BIG_BOARD_DIM