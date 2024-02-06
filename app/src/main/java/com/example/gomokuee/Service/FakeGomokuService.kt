package com.example.gomokuee.Service


import com.example.gomokuee.Domain.Board.*
import com.example.gomokuee.Domain.*
import kotlinx.coroutines.delay
import java.time.Instant

private const val FAKE_SERVICE_DELAY = 1000L
private const val FAKE_USER_TOKEN_LENGTH = 10
class FakeGomokuService : GomokuService {

    override suspend fun getGameById(token: String, gameId: String): Game {
        delay(FAKE_SERVICE_DELAY)
        val user = GomokuUsers.getUserByToken(token) ?: throw InvalidLogin()
        return GomokuGames.games.firstOrNull {
            it.gameId == gameId && (it.users.first == user || it.users.second == user)
        } ?: throw GameNotFound()
    }
    override suspend fun play(gameId: String, cell: Cell, boardSize: Int): Game {
        delay(FAKE_SERVICE_DELAY)
        val game = GomokuGames.games.firstOrNull {
            it.gameId == gameId
        } ?: throw GameNotFound()
        return game.computeNewGame(cell)
    }

    override suspend fun fetchFavourites(): List<FavInfo> {
        return GomokuFavourites.favourites
    }
}

object GomokuFavourites {
    private val _favourites: MutableList<FavInfo> = mutableListOf(
        FavInfo("Game1", "tbmaster", "05/02/2024"),
        FavInfo("Game2","jp","05/02/2024"),
        FavInfo("Game3","tiago","05/02/2024"),
    )

    val favourites : List<FavInfo>
        get() = _favourites.toList()
}


object GomokuUsers {
    private val _users: MutableList<User> = mutableListOf(
        User("1", "tbmaster", "jubas"),
        User("2", "jp", "paulinho"),
        User("3", "noobmaster69", "qwerty")
    )

    val users: List<User>
        get() = _users.toList()

    private val _passwords: MutableMap<String, String> = mutableMapOf(
        "1" to "jubas",
        "2" to "paulinho",
        "3" to "qwerty"
    )

    val passwords: Map<String, String>
        get() = _passwords.toMap()

    fun validateLogIn(username: String, password: String): User? =
        _users.firstOrNull { user ->
            user.username == username && _passwords[user.userId] == password
        }

    private val _tokens: MutableMap<String, String> = mutableMapOf()

    val tokens: Map<String, String>
        get() = _tokens.toMap()

    fun createToken(userId: String, username: String): UserInfo {
        val token = generateRandomString()
        _tokens[userId] = token
        return UserInfo(userId, username, token)
    }

    fun getUserByToken(token: String): User? {
        val id: String? = _tokens.entries.firstOrNull { (_, v) ->
            v == token
        }?.key
        return _users.firstOrNull { it.userId == id }
    }

    fun createUser(username: String, password: String): UserInfo? {
        if (users.firstOrNull { it.username == username } != null) return null
        val userId: String = generateRandomString()
        val token = generateRandomString()
        _users.add(User(userId, username, password))
        _passwords[userId] = password
        _tokens[userId] = token
        return UserInfo(userId, username, token)
    }
}

object GomokuGames {
    private val _games: MutableList<Game> = mutableListOf(
        Game(
            gameId = "1",
            users = Pair(User("2", "jp", "paulinho"), User("1", "tbmaster", "jubas")),
            board = createBoard(boardSize = BOARD_DIM),
            currentPlayer = Player(User("2", "jp", "paulinho"), Turn.BLACK_PIECE),
            rules = Rules(BOARD_DIM, Opening.FREESTYLE, Variant.FREESTYLE)
        )
    )

    val games: List<Game>
        get() = _games.toList()

    fun createGame(
        users: Pair<User, User>,
        board: Board,
        currentPlayer: Player,
        score: Int,
        now: Instant,
        rules: Rules
    ): Game {
        val gameId = generateRandomString()
        return Game(gameId, users, board, currentPlayer, rules)
    }
}

private fun generateRandomString(): String =
    (1..FAKE_USER_TOKEN_LENGTH)
        .map { ('a'..'z') + ('A'..'Z') + ('0'..'9').random() }
        .joinToString("")
