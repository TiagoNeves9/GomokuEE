package com.example.gomokuee.Service.utils

import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Domain.Board.deserializeToBoard
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.serialize
import com.example.gomokuee.Domain.toListBoard
import com.example.gomokuee.Domain.toPlayer
import com.example.gomokuee.Domain.toRules
import com.example.gomokuee.Domain.toUser
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

const val BOARD_FIELD = "board"
const val GAMEID_FIELD = "id"
const val PLAYER_FIELD = "player"
const val USER1_FIELD = "user1"
const val USER2_FIELD = "user2"
const val RULES_FIELD = "rules"
fun Game.toDocumentContent(): Map<String,String> = buildMap {
    put(BOARD_FIELD, board.serialize())
    put(GAMEID_FIELD, gameId)
    put(PLAYER_FIELD, currentPlayer.serialize())
    put(USER1_FIELD, users.first.serialize())
    put(USER2_FIELD, users.second.serialize())
    put(RULES_FIELD, rules.serialize())

}

fun DocumentSnapshot.toGameOrNull(): Game? {
    return if (data != null) {
        val board = (data!![BOARD_FIELD] as String).deserializeToBoard()
        val gameId = data!![GAMEID_FIELD].toString()
        val currentPlayer = (data!![PLAYER_FIELD] as String).toPlayer()
        val user1 = (data!![USER1_FIELD] as String).toUser()
        val user2 = (data!![USER2_FIELD] as String).toUser()
        val rules = (data!![RULES_FIELD] as String).toRules()

        Game(gameId, Pair(user1,user2),board,currentPlayer, rules)
    }
    else null
}

