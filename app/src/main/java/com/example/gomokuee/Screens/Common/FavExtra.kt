package com.example.gomokuee.Screens.Common

import android.content.Intent
import android.os.Parcelable
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Turn
import kotlinx.parcelize.Parcelize

const val FAVOURITE_EXTRA = "FavInfo"
@Parcelize
data class FavExtra(val title: String, val opponent: String, val date : String, val plays : List<BoardExtra>) : Parcelable{
    constructor(favInfo: FavInfo) : this(favInfo.title, favInfo.opponent, favInfo.date, favInfo.plays.map { BoardExtra(it) })
}

fun FavExtra.toFavInfo() : FavInfo {
    return FavInfo(title,opponent, date, plays.map {
        if (it.positions.isEmpty()) it.toBoard(Turn.BLACK_PIECE)
        else it.toBoard(it.positions.toList().last().second)
    })
}

@Suppress("DEPRECATION")
fun getFavInfoExtra(intent: Intent): FavExtra? =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
        intent.getParcelableExtra(FAVOURITE_EXTRA, FavExtra::class.java)
    else
        intent.getParcelableExtra(FAVOURITE_EXTRA)