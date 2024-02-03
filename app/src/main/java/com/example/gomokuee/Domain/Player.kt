package com.example.gomokuee.Domain

/**
 * Player is a Pair of
 * User (person with an account) and Turn (color of the user's pieces)
 * */
typealias Player = Pair<User, Turn>

data class User(
    val userId: String,
    val username: String,
    val encodedPassword: String
)

enum class Turn {
    BLACK_PIECE, WHITE_PIECE;

    fun other() = if (this == WHITE_PIECE) BLACK_PIECE else WHITE_PIECE
}