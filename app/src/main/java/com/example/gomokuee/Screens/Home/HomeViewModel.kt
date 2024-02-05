package com.example.gomokuee.Screens.Home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.gomokuee.Domain.UserInfoRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repo : UserInfoRepository): ViewModel() {

    companion object{
        fun factory(repo: UserInfoRepository) = viewModelFactory {
            initializer { HomeViewModel(repo) }
        }
    }
    private var _error: Exception? by mutableStateOf(null)

    val error: Exception?
        get() = _error

    fun onDismissError() = viewModelScope.launch {
        _error = null
    }

}