package com.example.gomokuee.Domain.Board

class Row private constructor(val number: Int) {
    val index = number - 1

    companion object {
        val valuesMedium = List(BOARD_DIM) { Row(it + 1) }
        val valuesBig = List(BIG_BOARD_DIM) { Row(it + 1) }

        operator fun invoke(number: Int, boardSize: Int = BOARD_DIM) =
            if (boardSize == BOARD_DIM) valuesMedium.first { it.number == number }
            else valuesBig.first { it.number == number }
    }

    override fun toString() = "Row $number."
}

fun Int.toRowOrNull(boardSize: Int) =
    if (boardSize == BOARD_DIM) Row.valuesMedium.find { it.number == this }
    else Row.valuesBig.find { it.number == this }

fun Int.toRow(boardSize: Int) =
    this.toRowOrNull(boardSize) ?: throw IllegalArgumentException("Invalid row $this.")

fun Int.indexToRowOrNull(boardSize: Int) =
    if (boardSize == BOARD_DIM) Row.valuesMedium.find { it.index == this }
    else Row.valuesBig.find { it.index == this }

fun Int.indexToRow(boardSize: Int) =
    this.indexToRowOrNull(boardSize) ?: throw IllegalArgumentException("Invalid row $this.")