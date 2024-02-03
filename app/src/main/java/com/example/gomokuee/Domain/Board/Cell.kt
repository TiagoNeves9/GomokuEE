package com.example.gomokuee.Domain.Board

import java.lang.Integer.max


class Cell private constructor(val row: Row, val col: Column) {
    val rowIndex: Int = row.index
    val colIndex: Int = col.index

    override fun toString(): String =
        if (this == INVALID) "INVALID Cell!" else "${this.row.number}${this.col.symbol}"

    companion object {
        val valuesMedium = List(BOARD_DIM * BOARD_DIM) {
            Cell(
                (it / BOARD_DIM).indexToRow(BOARD_DIM),
                (it % BOARD_DIM).indexToColumn(BOARD_DIM)
            )
        }
        val valuesBig = List(BIG_BOARD_DIM * BIG_BOARD_DIM) {
            Cell(
                (it / BIG_BOARD_DIM).indexToRow(BIG_BOARD_DIM),
                (it % BIG_BOARD_DIM).indexToColumn(BIG_BOARD_DIM)
            )
        }
        val INVALID = Cell(-1, -1, -1)

        operator fun invoke(rowIndex: Int, colIndex: Int, boardSize: Int): Cell =
            if (rowIndex in 0 until boardSize && colIndex in 0 until boardSize) {
                if (boardSize == BOARD_DIM) valuesMedium[rowIndex * boardSize + colIndex]
                else valuesBig[rowIndex * boardSize + colIndex]
            } else INVALID

        operator fun invoke(row: Row, col: Column, boardSize: Int = BOARD_DIM): Cell =
            Cell(row.index, col.index, boardSize)
    }
}

fun String.toCellOrNull(boardSize: Int): Cell? {
    if (this.length < 2 || this.length > 3) return null
    val aux = if (this.length == 2) "0$this" else this
    if (!aux[1].isDigit()) return null

    val row = aux.dropLast(1).toInt().toRowOrNull(boardSize)
    val col = aux[2].toColumnOrNull(boardSize)
    return if (boardSize == BOARD_DIM) Cell.valuesMedium.find { it.row == row && it.col == col }
    else Cell.valuesBig.find { it.row == row && it.col == col }
}

fun Cell.plus(dir: Direction, boardSize: Int): Cell =
    Cell(row.index + dir.difRow, col.index + dir.difCol, boardSize)


fun String.toCell(boardSize: Int): Cell =
    this.toCellOrNull(boardSize) ?: throw IllegalArgumentException(this.getFailReason())

private fun String.getFailReason(): String {
    if (this.length != 2) return "Cell must have a row and a column."

    val rowNr = this.substring(0, this.length - 1)
    val colLetter = this[1]

    return if (rowNr.toInt() !in 1..BOARD_DIM) "Invalid row $rowNr."
    else if (colLetter.code - 'A'.code !in 0 until BOARD_DIM) "Invalid column $colLetter."
    else ""
}

fun cellsInDir(from: Cell, dir: Direction, boardSize: Int): List<Cell> {
    val line = mutableListOf<Cell>()
    var currentCell = from.plus(dir, boardSize)
    while (currentCell != Cell.INVALID) {
        line.add(currentCell)
        currentCell = currentCell.plus(dir, boardSize)
    }
    return line
}

fun Cell.distance(other: Cell): Int = max(
    kotlin.math.abs(this.rowIndex - other.rowIndex),
    kotlin.math.abs(this.colIndex - other.colIndex)
)