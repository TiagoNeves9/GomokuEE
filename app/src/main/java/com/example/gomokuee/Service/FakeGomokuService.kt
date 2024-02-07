package com.example.gomokuee.Service


import com.example.gomokuee.Domain.Board.*
import com.example.gomokuee.Domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant

private const val FAKE_SERVICE_DELAY = 1000L
private const val FAKE_USER_TOKEN_LENGTH = 10
class FakeGomokuService : GomokuService {

    override suspend fun getGameById(gameId: String): Flow<Game> = flow {
        delay(FAKE_SERVICE_DELAY)
        val game =  GomokuGames.games.firstOrNull {
            it.gameId == gameId
        } ?: throw GameNotFound()
        emit(game)
    }
    override suspend fun play(gameId: String, cell: Cell, boardSize: Int): Flow<Game> = flow {
        delay(FAKE_SERVICE_DELAY)
        val game = GomokuGames.games.firstOrNull {
            it.gameId == gameId
        } ?: throw GameNotFound()
        val newGame = game.computeNewGame(cell)
        GomokuFavourites.updatePlays(newGame)
        GomokuGames.updateGame(newGame,game)
        emit(newGame)
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
    var favouritesPlays : List<Board> = emptyList()
    fun updatePlays(game: Game): List<Board>{
        val boardToAdd = BoardRun(game.board.positions,game.currentPlayer.second, BOARD_DIM)
        favouritesPlays = favouritesPlays + boardToAdd
        return favouritesPlays
    }

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

    fun updateGame(gameNew: Game, gameOld: Game){
        _games.remove(gameOld)
        _games.add(gameNew)
    }

    val games: List<Game>
        get() = _games.toList()

    fun createGame(
        users: Pair<User, User> = Pair(User("1","PlayerB","admin"), User("2","PlayerW","admin")),
        board: Board = createBoard(boardSize = BOARD_DIM),
        currentPlayer: Player = Player(User("1","PlayerB","admin"), Turn.BLACK_PIECE),
        rules: Rules = Rules(BOARD_DIM, Opening.FREESTYLE, Variant.FREESTYLE)
    ): Game {
        val gameId = generateRandomString()
        val game = Game(gameId, users, board, currentPlayer, rules)
        _games.add(game)
        return game
    }
}

private fun generateRandomString(): String =
    (1..FAKE_USER_TOKEN_LENGTH)
        .map { ('a'..'z') + ('A'..'Z') + ('0'..'9').random() }
        .joinToString("")
