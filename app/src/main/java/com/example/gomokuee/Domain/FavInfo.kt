package com.example.gomokuee.Domain

data class FavInfo(val title: String, val opponent: String, val date : String){
    init {
        require(validateFavInfo(title, opponent, date))
    }
}

fun validateFavInfo(title: String, opponent: String, date:String): Boolean {
    return title.isNotBlank() && opponent.isNotBlank() && date.isNotBlank()
}

fun toFavInfoOrNull(title: String,opponent: String,date: String): FavInfo? =
    if (validateFavInfo(title, opponent, date))
        FavInfo(title, opponent, date)
    else
        null