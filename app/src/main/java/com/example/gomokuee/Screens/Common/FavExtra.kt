package com.example.gomokuee.Screens.Common

import android.content.Intent
import android.os.Parcelable
import com.example.gomokuee.Domain.FavInfo
import kotlinx.parcelize.Parcelize

const val FAVOURITE_EXTRA = "FavInfo"
@Parcelize
data class FavExtra(val title: String, val opponent: String, val date : String) : Parcelable{
    constructor(favInfo: FavInfo) : this(favInfo.title, favInfo.opponent, favInfo.date)
}

fun FavExtra.toFavInfo() = FavInfo(title,opponent, date)

@Suppress("DEPRECATION")
fun getFavInfoExtra(intent: Intent): FavExtra? =
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
        intent.getParcelableExtra(FAVOURITE_EXTRA, FavExtra::class.java)
    else
        intent.getParcelableExtra(FAVOURITE_EXTRA)