package com.example.gomokuee.Domain

import kotlinx.coroutines.flow.Flow


sealed interface FavouritesEvent{
    data class FavouritesUpdate(val favourites: List<FavInfo>) : FavouritesEvent
}




interface FavInfoRepository {
    suspend fun saveFavourite(favInfo: FavInfo): Flow<FavouritesEvent>

}