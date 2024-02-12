package com.example.gomokuee.Storage

import com.example.gomokuee.Domain.Board.Board
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.FavInfoRepository
import com.example.gomokuee.Domain.FavouritesEvent
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FavsFirebase(private val db: FirebaseFirestore) : FavInfoRepository {

    private var state: FavouriteState = FavouriteState.Idle
    override suspend fun saveFavourite(favInfo: FavInfo): Flow<FavouritesEvent> {
        check(state is FavouriteState.Idle) { "The game is already saved "}
        state = FavouriteState.Saving

        return callbackFlow {
            val favouriteDocRef  = getFavouriteDocRef(favInfo)
            state = FavouriteState.Saved(favInfo,favouriteDocRef,this)
            val favouritesUpdateSubcription = subscribeFavouritesUpdated(this)

            favouriteDocRef.set(favInfo.toDocumentContent()).await()

            awaitClose {
                favouritesUpdateSubcription.remove()
                favouriteDocRef.delete()
                state = FavouriteState.Idle
            }

        }
    }


    private fun subscribeFavouritesUpdated(flow : ProducerScope<FavouritesEvent>) =
        db.collection(FAVOURITE).addSnapshotListener { snapshot, error ->
            when{
                error != null -> flow.close(error)
                snapshot != null -> flow.trySend(FavouritesEvent.FavouritesUpdate(snapshot.toFavouritesList()))
            }
        }

    private fun getFavouriteDocRef(favourite: FavInfo) =
        db.collection(FAVOURITE).document(favourite.id.toString())
}

private sealed interface FavouriteState {
    data object Idle : FavouriteState

    data object Saving : FavouriteState

    data class Saved(
        val favourite : FavInfo,
        val favouriteDocRef : DocumentReference,
        val producer: ProducerScope<FavouritesEvent>
    ) : FavouriteState
}

const val FAVOURITE = "favourite"
const val TITLE_FIELD = "title"
const val OPPONENT_FIELD = "opponent"
const val PLAYS_FIELD = "plays"
const val DATE_FIELD = "date"
const val FAVID_FIELD = "id"

fun QueryDocumentSnapshot.toFavouriteInfo() =
    FavInfo(
        title = data[TITLE_FIELD] as String,
        opponent = data[OPPONENT_FIELD] as String,
        plays = data[PLAYS_FIELD] as List<Board>,
        date = data[DATE_FIELD] as String,
        id = UUID.fromString(id)
    )

fun QuerySnapshot.toFavouritesList() = map { it.toFavouriteInfo() }

fun FavInfo.toDocumentContent() = mapOf(
    TITLE_FIELD to title,
    OPPONENT_FIELD to opponent,
    PLAYS_FIELD to plays,
    DATE_FIELD to date
)