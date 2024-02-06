package com.example.gomokuee.Screens.Replay

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.getOrNull
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.Domain.loading
import com.example.gomokuee.Service.GomokuService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReplayScreenViewModel(private val service: GomokuService): ViewModel() {
    companion object{
        fun factory(service: GomokuService) = viewModelFactory {
            initializer { ReplayScreenViewModel(service) }
        }
    }

    private val _favouritesListFlow: MutableStateFlow<LoadState<List<FavInfo>?>> = MutableStateFlow(idle())

    val favouritesList: Flow<LoadState<List<FavInfo>?>>
        get() = _favouritesListFlow.asStateFlow()

    private var _index: Int by mutableIntStateOf(0)

    val index: Int
        get() = _index

    fun next() = viewModelScope.launch {
        _favouritesListFlow.value.getOrNull()?.let { favInfos ->
            _index = if (_index == favInfos.size - 1) 0 else _index + 1
        }
    }

    fun prev() = viewModelScope.launch {
        _favouritesListFlow.value.getOrNull()?.let { favInfos ->
            _index = if (_index == 0) favInfos.size - 1 else _index - 1
        }
    }

    fun fetchFavourites(){
        _favouritesListFlow.value = loading()
        viewModelScope.launch {
            val result = runCatching { service.fetchFavourites() }
            _favouritesListFlow.value = loaded(result)
        }
    }
}