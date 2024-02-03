package com.example.gomokuee.Domain

import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.Board.BoardDraw
import com.example.gomokuee.Domain.Board.BoardWin
import com.example.gomokuee.Domain.Board.Cell


data class Game(
    val gameId: String, val users: Pair<User, User>, val board: Board,
    val currentPlayer: Player, val rules: Rules
) {
    private fun switchTurn() =
        if (currentPlayer.first == users.first) users.second
        else users.first

    fun computeNewGame(cell: Cell): Game {
        val newBoard = this.board.addPiece(cell)

        return if (newBoard.checkWin(cell))
            this.copy(board = BoardWin(newBoard.positions, this.currentPlayer, this.rules.boardDim))
        else if (newBoard.checkDraw(this.board.boardSize))
            this.copy(board = BoardDraw(newBoard.positions, this.rules.boardDim))
        else this.copy(
            board = newBoard,
            currentPlayer = Player(this.switchTurn(), this.currentPlayer.second.other())
        )
    }
}