package com.example.gomokuee.Service

import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.serialize
import com.example.gomokuee.Domain.toListBoard
import com.example.gomokuee.Service.utils.FirestoreExtensions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class FirestoreService(
    private val db: FirebaseFirestore
) : GomokuService {
    override suspend fun play(gameId: String, cell: Cell, boardSize: Int): Flow<Game> = flow {
        val game = GomokuGames.games.firstOrNull {
            it.gameId == gameId
        } ?: throw GameNotFound()
        val newGame = game.computeNewGame(cell)
        GomokuFavourites.updatePlays(newGame)
        GomokuGames.updateGame(newGame,game)
        emit(newGame)
    }

    override suspend fun updateFavInfo(title: String, opponent: String): FavInfo {
        var favouriteDoc: DocumentReference? = null
        val plays = GomokuFavourites.favouritesPlays
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH) + 1
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val date = "$day-$month-$year $hour:$minute"

        try {
            favouriteDoc = db.collection(FirestoreExtensions.FavouritesCollection).add(
                hashMapOf(
                    FirestoreExtensions.FavouritesTitleField to title,
                    FirestoreExtensions.FavouritesOpponentField to opponent,
                    FirestoreExtensions.FavouritesDateField to date,
                    FirestoreExtensions.FavouritesPlaysField to plays.serialize()
                )
            ).await()
        }catch (ex : Exception){
            if (favouriteDoc != null)
                favouriteDoc.delete().await()
            throw ex
        }
        dismissPlays()
        return FirestoreExtensions.mapToFavourite(favouriteDoc.get().await())
    }

    override suspend fun dismissPlays() {
        GomokuFavourites.resetPlays()
    }

    override suspend fun fetchFavourites(): List<FavInfo> {
       return db.collection(FirestoreExtensions.FavouritesCollection)
           .get()
           .await()
           .map {
               FavInfo(
                   title = it.getString(FirestoreExtensions.FavouritesTitleField)!!,
                   opponent = it.getString(FirestoreExtensions.FavouritesOpponentField)!!,
                   date = it.getString(FirestoreExtensions.FavouritesDateField)!!,
                   plays = it.getString(FirestoreExtensions.FavouritesPlaysField)!!.toListBoard(),
               )
           }
    }

}

/**
 * The name of the collection for ongoing games
 */
const val ONGOING = "ongoing"