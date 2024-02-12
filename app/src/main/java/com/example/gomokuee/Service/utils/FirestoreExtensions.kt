package com.example.gomokuee.Service.utils


import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.toListBoard
import com.google.firebase.firestore.DocumentSnapshot

class FirestoreExtensions{
    companion object{

        const val FavouritesCollection = "favourites"
        const val FavouritesTitleField = "title"
        const val FavouritesOpponentField = "opponent"
        const val FavouritesDateField = "date"
        const val FavouritesPlaysField = "plays"
        const val FavouritesIdField = "id"

        const val GamesCollection = "games"

        fun mapToFavourite(favDoc: DocumentSnapshot): FavInfo{
            return FavInfo(
                title = favDoc.getString(FavouritesTitleField)!!,
                opponent = favDoc.getString(FavouritesOpponentField)!!,
                date = favDoc.getString(FavouritesDateField)!!,
                plays = favDoc.getString(FavouritesPlaysField)!!.toListBoard()
            )
        }
    }

}