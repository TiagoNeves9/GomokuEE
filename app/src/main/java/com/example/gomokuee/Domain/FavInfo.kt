package com.example.gomokuee.Domain

import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Screens.Common.BoardExtra

data class FavInfo(val title: String, val opponent: String, val date : String, val plays: List<Board>){
    init {
        require(validateFavInfo(title, opponent, date))
    }
}

fun validateFavInfo(title: String, opponent: String, date:String): Boolean {
    return title.isNotBlank() && opponent.isNotBlank() && date.isNotBlank()
}

fun toFavInfoOrNull(title: String,opponent: String,date: String, plays: List<Board>): FavInfo? =
    if (validateFavInfo(title, opponent, date))
        FavInfo(title, opponent, date, plays)
    else
        null