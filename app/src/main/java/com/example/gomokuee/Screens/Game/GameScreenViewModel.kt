package com.example.gomokuee.Screens.Game

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.Board.BoardDraw
import com.example.gomokuee.Domain.Board.BoardRun
import com.example.gomokuee.Domain.Board.BoardWin
import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.Player
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Domain.failure
import com.example.gomokuee.Domain.getOrNull
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.Domain.loading
import com.example.gomokuee.Domain.success
import com.example.gomokuee.Service.GomokuService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant

class GameScreenViewModel(
    private val service : GomokuService,
    private val gameInfo: Game,
) : ViewModel() {
    companion object {
        fun factory(service: GomokuService, gameInfo: Game) = viewModelFactory {
            initializer { GameScreenViewModel(service,gameInfo) }
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private var _selectedCell: Cell? by mutableStateOf(null)

    val selectedCell: Cell?
        get() =  _selectedCell

    fun changeSelectedCell(cell: Cell) = viewModelScope.launch {
        _selectedCell = cell
    }

    private val _currentGameFlow: MutableStateFlow<LoadState<Game?>> = MutableStateFlow(success(gameInfo))

    val currentGameFlow: Flow<LoadState<Game?>>
        get() = _currentGameFlow.asStateFlow()

    fun resetCurrentGameFlowFlowToIdle() {
        _currentGameFlow.value = idle()
    }

    private var _startedAt: Long by mutableStateOf(0)
    private var _time: Long by mutableStateOf(30)
    private var _running: Boolean by mutableStateOf(false)

    val time: Long
        get() = _time

    val startAt: Long
        get() = _startedAt

    val running
        get() = _running

    fun startTimer(){
        _running = true
        _time = 30
        _startedAt = System.currentTimeMillis()
        viewModelScope.launch {
            while (running) {
                if (_time <= 0) {
                    stopTimer()
                    winnerOnTimeout()
                }
                delay(1000)
                _time -= 1
            }
        }
    }

    fun resetTimer(){
        _time = 30
        _startedAt = System.currentTimeMillis()
    }

    fun stopTimer(){
        _running = false
        _time = 0
    }

    fun winnerOnTimeout(){

        val game = _currentGameFlow.value.getOrNull()

        if(game != null){
            val player = if (game.currentPlayer.first  == game.users.first) Player(game.users.second, game.currentPlayer.second.other())
            else{
                Player(game.users.first,game.currentPlayer.second.other())
            }
            _currentGameFlow.value = success(game.copy(board = BoardWin(game.board.positions,player,game.rules.boardDim)))
        }

    }




    fun fetchGame() {
        _currentGameFlow.value = loading()
        scope.launch {
            try {
                service.getGameById(gameInfo.gameId).collect{ game ->
                    _currentGameFlow.value = loaded(Result.success(game))
                }
            }catch (cause: Throwable){
                _currentGameFlow.value = failure(cause)
            }
        }
    }

    fun play() {
        check(_selectedCell != null)
        //_currentGameFlow.value = loading()
        viewModelScope.launch {
            _selectedCell?.let {cell ->
                try {
                    service.play(gameInfo.gameId,cell,gameInfo.rules.boardDim).collect{ game ->
                        _currentGameFlow.value = loaded(Result.success(game))
                        _selectedCell = null
                        if (game.board is BoardRun) resetTimer()
                        else stopTimer()
                    }


                }catch (cause: Throwable){
                    _currentGameFlow.value = failure(cause)
                }

            }
        }
    }
}