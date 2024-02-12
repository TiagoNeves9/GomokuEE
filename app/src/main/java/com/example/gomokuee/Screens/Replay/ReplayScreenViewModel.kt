package com.example.gomokuee.Screens.Replay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.Board.Board
import kotlinx.coroutines.launch

class ReplayScreenViewModel(private val plays : List<Board>): ViewModel() {
    companion object{
        fun factory(plays: List<Board>) = viewModelFactory {
            initializer { ReplayScreenViewModel(plays) }
        }
    }

    private var _index: Int by mutableIntStateOf(0)

    val index: Int
        get() = _index

    fun next() = viewModelScope.launch {

        _index = if (_index == plays.size - 1) _index else _index + 1

    }

    fun prev() = viewModelScope.launch {
        _index = if (_index == 0) _index else _index - 1
    }

}