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

fun User.serialize() : String {
    val userID = this.userId
    val username = this.username
    val encodedPassword = this.encodedPassword

    return "$userID\n$username\n$encodedPassword"
}

fun String.toUser() : User{
    val words = this.split("\n")
    val userId = words[0]
    val username = words[1]
    val encodedPassword = words[2]
    return User(userId,username, encodedPassword)
}

fun Player.serialize() : String {
    val userID = this.first.userId
    val username = this.first.username
    val encodedPassword = this.first.encodedPassword
    val turn = this.second.toString()
    return "$userID\n$username\n$encodedPassword\n$turn"
}

fun String.toPlayer() : Player{
    val words = this.split("\n")
    val userId = words[0]
    val username = words[1]
    val encodedPassword = words[2]
    val turn = if (words[0] == Turn.BLACK_PIECE.toString()) Turn.BLACK_PIECE else Turn.WHITE_PIECE
    val user = User(userId,username, encodedPassword)
    return Player(user,turn)
}

enum class Turn {
    BLACK_PIECE, WHITE_PIECE;

    fun other() = if (this == WHITE_PIECE) BLACK_PIECE else WHITE_PIECE
}