package com.example.gomokuee.Service

import com.example.gomokuee.Service.utils.ProblemJson
import kotlinx.coroutines.flow.Flow


interface GomokuService {

}

/**
 * Represents an error that occurred while fetching a joke.
 * @param message The error message
 * @param cause The cause of the error, if any
 */
abstract class FetchGomokuException(message: String, cause: Throwable? = null)
    : Exception(message, cause)

class UserAlreadyExists : FetchGomokuException("user already exists")

class InvalidLogin : FetchGomokuException("invalid credentials")

class UnknownLobby : FetchGomokuException("Lobby not found")

class UnknownUser : FetchGomokuException("User not found")

class GameNotFound : FetchGomokuException("Game not found")

class FetchGomokuError(message: String, cause: Throwable?) : FetchGomokuException(message, cause)

/*class UnexpectedResponseException(
    contentType: MediaType? = null,
) : FetchGomokuException(message = "Unexpected content type [$contentType] response from the API.")
*/
class ApiUnauthorizedException : FetchGomokuException("Unauthorized Access")

class ApiErrorException(
    val problemJson: ProblemJson
) : FetchGomokuException(message = problemJson.detail ?: "Something went wrong")