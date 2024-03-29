package com.example.gomokuee.Domain

import android.util.Log
import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.Board.deserializeToBoard
import java.util.UUID

data class FavInfo(val title: String, val opponent: String, val date : String, val plays: List<Board>, val id: UUID = UUID.randomUUID()){
    init {
        require(validateFavInfo(title, opponent, date))
    }
}

fun validateFavInfo(title: String, opponent: String, date:String): Boolean {
    return title.isNotBlank() && opponent.isNotBlank() && date.isNotBlank()
}

fun List<Board>.serialize() : String {
    return this.joinToString(separator = "<>") { it.serialize()}
}

fun String.toListBoard() : List<Board>{
    val boards = this.split("<>")
    val list = mutableListOf<Board>()
    var i = 0
    while (i < boards.size){
        val board = boards[i].deserializeToBoard()
        list.add(board)
        i++
    }

    return list
}