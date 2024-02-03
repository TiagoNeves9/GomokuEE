package com.example.gomokuee.Domain.Board

class Column private constructor(val symbol: Char) {
    val index get() = symbol - 'A'

    companion object {
        val valuesMedium = List(BOARD_DIM) { Column('A' + it) }
        val valuesBig = List(BIG_BOARD_DIM) { Column('A' + it) }

        operator fun invoke(symbol: Char, boardSize: Int = BOARD_DIM) =
            if (boardSize == BOARD_DIM) valuesMedium.first { it.symbol == symbol }
            else valuesBig.first { it.symbol == symbol }
    }

    override fun toString() = "Column $symbol."
}

fun Char.toColumnOrNull(boardSize: Int) =
    if (boardSize == BOARD_DIM) Column.valuesMedium.find { it.symbol == this }
    else Column.valuesBig.find { it.symbol == this }

fun Char.toColumn(boardSize: Int) =
    this.toColumnOrNull(boardSize) ?: throw IllegalArgumentException("Invalid column $this.")

fun Int.indexToColumnOrNull(boardSize: Int) =
    if (boardSize == BOARD_DIM) Column.valuesMedium.find { it.index == this }
    else Column.valuesBig.find { it.index == this }

fun Int.indexToColumn(boardSize: Int) =
    this.indexToColumnOrNull(boardSize) ?: throw IllegalArgumentException("Invalid column $this.")