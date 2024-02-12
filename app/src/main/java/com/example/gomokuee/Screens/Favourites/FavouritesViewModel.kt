package com.example.gomokuee.Screens.Favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.Domain.loading
import com.example.gomokuee.Service.GomokuService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavouritesViewModel( private val service: GomokuService) : ViewModel() {

    companion object{
        fun factory( service: GomokuService) = viewModelFactory {
            initializer { FavouritesViewModel(service) }
        }
    }

    private val _favouritesFlow: MutableStateFlow<LoadState<List<FavInfo>>> = MutableStateFlow(idle())

    val favourites: Flow<LoadState<List<FavInfo>>>
        get() = _favouritesFlow.asStateFlow()

    fun resetToIdle(){
        _favouritesFlow.value = idle()
    }

    fun fetchFavourites(){
        _favouritesFlow.value = loading()
        viewModelScope.launch {
            val result = runCatching { service.fetchFavourites() }
            Log.v("FAVS55", service.fetchFavourites().toString())
            _favouritesFlow.value = loaded(result)
        }
    }
}