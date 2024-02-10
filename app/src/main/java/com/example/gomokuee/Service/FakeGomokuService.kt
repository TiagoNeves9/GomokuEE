package com.example.gomokuee.Service


import com.example.gomokuee.Domain.Board.*
import com.example.gomokuee.Domain.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar

private const val FAKE_USER_TOKEN_LENGTH = 10
class FakeGomokuService : GomokuService {

    override suspend fun getGameById(gameId: String): Flow<Game> = flow {
        val game =  GomokuGames.games.firstOrNull {
            it.gameId == gameId
        } ?: throw GameNotFound()
        emit(game)
    }
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
        return GomokuFavourites.saveFav(title, opponent)
    }

    override suspend fun dismissPlays() {
        GomokuFavourites.resetPlays()
    }
    override suspend fun fetchFavourites(): List<FavInfo> {
        return GomokuFavourites.favourites
    }
}

object GomokuFavourites {

    val cell1: Cell = Cell.invoke(11, 4, BOARD_DIM)
    val cell2: Cell = Cell.invoke(10, 6, BOARD_DIM)
    val map: Map<Cell,Turn> = mapOf(Pair(cell1,Turn.BLACK_PIECE))
    val plays : List<Board> = listOf(
        BoardRun(emptyMap(),Turn.BLACK_PIECE, BOARD_DIM),
        BoardRun(map,Turn.BLACK_PIECE, BOARD_DIM),
        BoardRun(map + mapOf(Pair(cell2,Turn.WHITE_PIECE)),Turn.WHITE_PIECE, BOARD_DIM),
    )

    private val _favourites: MutableList<FavInfo> = mutableListOf(
        FavInfo("Game1", "tbmaster", "05/02/2024", plays),
        FavInfo("Game2","jp","05/02/2024", plays),
        FavInfo("Game3","tiago","05/02/2024", plays),
    )

    val favourites : List<FavInfo>
        get() = _favourites.toList()


    private var favouritesPlays : List<Board> = emptyList()
    fun updatePlays(game: Game): List<Board>{
        val boardToAdd = BoardRun(game.board.positions,game.currentPlayer.second, BOARD_DIM)
        favouritesPlays = favouritesPlays + boardToAdd
        return favouritesPlays
    }

    fun saveFav(title: String, opponent: String): FavInfo{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)
        val date = "$day-$month-$year $hour:$minute"
        val plays = favouritesPlays

        val newFavGame = FavInfo(title, opponent, date, plays)

        _favourites.add(newFavGame)

        resetPlays()

        return newFavGame
    }

    fun resetPlays(){
        favouritesPlays = emptyList()
    }


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
