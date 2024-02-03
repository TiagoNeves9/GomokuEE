package com.example.gomokuee.Screens.Game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.Board.Cell
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Service.GomokuService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameScreenViewModel(
    private val service : GomokuService,
    private val gameInfo: Game
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

    private val _currentGameFlow: MutableStateFlow<LoadState<Game?>> = MutableStateFlow(idle())

    val currentGameFlow: Flow<LoadState<Game?>>
        get() = _currentGameFlow.asStateFlow()

    fun resetCurrentGameFlowFlowToIdle() {
        _currentGameFlow.value = idle()
    }
}