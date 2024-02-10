package com.example.gomokuee.Screens.NewFavourite

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Idle
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.Loading
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.Domain.loading
import com.example.gomokuee.Screens.Components.EmptyOpponent
import com.example.gomokuee.Screens.Components.EmptyTitle
import com.example.gomokuee.Service.GomokuService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewFavouriteViewModel(
    private val service: GomokuService
): ViewModel() {
    companion object{
        fun factory(service: GomokuService) = viewModelFactory {
            initializer { NewFavouriteViewModel(service) }
        }
    }

    private var _title by mutableStateOf("")

    val title
        get() = _title

    fun changeTitle(title: String) = viewModelScope.launch {
        _title = title.replace("\\s".toRegex(), "")
    }

    private var _opponent by mutableStateOf("")

    val opponent
        get() = _opponent

    fun changeOpponent(opponent: String) = viewModelScope.launch {
        _opponent = opponent.replace("\\s".toRegex(), "")
    }



    private val _favStateFlow : MutableStateFlow<LoadState<FavInfo?>> = MutableStateFlow(idle())


    val favStateFlow: Flow<LoadState<FavInfo?>>
        get() = _favStateFlow.asStateFlow()

    fun resetToIdle(){
        if (_favStateFlow.value !is Loading)
            throw IllegalStateException("The view model is not in the saving state.")
        _favStateFlow.value = idle()
    }

    fun saveFavourite(){
        if (_favStateFlow.value is Idle){
            _favStateFlow.value = loading()
            viewModelScope.launch {
                val result: Result<FavInfo> = when{
                    title.isBlank() -> Result.failure(EmptyTitle)
                    opponent.isBlank() -> Result.failure(EmptyOpponent)
                    else -> kotlin.runCatching {
                        val favInfo = service.updateFavInfo(title,opponent)
                        favInfo
                    }
                }
                _favStateFlow.value = loaded(result)
            }
        }
    }

    fun dismiss(){
        viewModelScope.launch {
            service.dismissPlays()
            resetToIdle()
        }
    }
}