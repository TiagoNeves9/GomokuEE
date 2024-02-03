package com.example.gomokuee.Domain

import com.example.gomokuee.Domain.Board.BIG_BOARD_DIM
import com.example.gomokuee.Domain.Board.BOARD_DIM
import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.Board.distance

data class Rules(val boardDim: Int, val opening: Opening, val variant: Variant)

enum class Opening {
    FREESTYLE, PRO;

    /** This function checks if the opening pro rules are respected */
    fun isProOpening(boardRun: BoardRun, cell: Cell): Boolean {
        if (this == PRO && boardRun.positions.size <= 2) {
            val centralCell = Cell(
                boardRun.boardSize / 2,
                boardRun.boardSize / 2,
                boardRun.boardSize
            )
            // first move must be in the center
            if (boardRun.positions.isEmpty() && cell != centralCell)
                throw IllegalArgumentException(
                    "First move must be in the center of the board! " +
                            "(Coordinates - $centralCell)"
                )
            // second move (first of second player) can be anywhere
            // third move (second of first player) must be at least 3 intersections away
            if (boardRun.positions.size == 2 && cell.distance(centralCell) < 3)
                throw IllegalArgumentException(
                    "Second move (first of second player) must be " +
                            "at least 3 intersections away " +
                            "from the first move (center of the board - $centralCell)! "
                )
        }
        return true
    }
}

val openingsList = listOf("Freestyle", "Pro")

val variantsList = listOf("Freestyle", "Swap after first")

enum class Variant {
    FREESTYLE, SWAP_AFTER_FIRST;
}

fun String.toOpening(): Opening =
    when (this.uppercase()) {
        /** no restrictions */
        "FREESTYLE" -> Opening.FREESTYLE

        /** The first player's first stone must be placed in the center of the board.
         *  The second player's first stone may be placed anywhere on the board.
         *  The first player's second stone must be placed at least three intersections
         *  away from the first stone (two empty intersections in between the two stones).
         *  */
        "PRO" -> Opening.PRO

        /** more openings can be implemented */
        else -> throw Exception()
    }

val boardSizeList = listOf(BOARD_DIM, BIG_BOARD_DIM)

fun Opening.toOpeningString(): String = when(this) {
    Opening.FREESTYLE -> "Freestyle"
    Opening.PRO -> "Pro"
}

fun Variant.toVariantString(): String = when(this) {
    Variant.FREESTYLE -> "Freestyle"
    Variant.SWAP_AFTER_FIRST -> "Swap after first"
}

fun String.toVariant(): Variant =
    when (this.uppercase()) {
        /** no restrictions */
        "FREESTYLE" -> Variant.FREESTYLE

        /** The first player's first stone must be placed in the center of the board.
         *  The second player's first stone may be placed anywhere on the board.
         *  The first player's second stone must be placed at least three intersections
         *  away from the first stone (two empty intersections in between the two stones).
         *  */
        "SWAP AFTER FIRST" -> Variant.SWAP_AFTER_FIRST

        /** more variants can be implemented */
        else -> throw Exception()
    }