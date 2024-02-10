package com.example.gomokuee.Service

import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Service.utils.ProblemJson
import kotlinx.coroutines.flow.Flow


interface GomokuService {

    suspend fun getGameById(gameId: String): Flow<Game>
    suspend fun play(gameId: String, cell: Cell, boardSize: Int): Flow<Game>

    suspend fun updateFavInfo(title: String, opponent: String): FavInfo

    suspend fun dismissPlays()
    suspend fun fetchFavourites() : List<FavInfo>
}

/**
 * Represents an error that occurred while fetching a joke.
 * @param message The error message
 * @param cause The cause of the error, if any
 */
abstract class FetchGomokuException(message: String, cause: Throwable? = null)
    : Exception(message, cause)

class GameNotFound : FetchGomokuException("Game not found")

class ApiUnauthorizedException : FetchGomokuException("Unauthorized Access")