package com.example.gomokuee.Screens.NewFavourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import com.example.gomokuee.Domain.Loaded
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Favourites.FavouritesActivity
import com.example.gomokuee.ui.theme.GomokuEETheme
import kotlinx.coroutines.launch

class NewFavouriteActivity: ComponentActivity() {

    companion object{
        fun navigateTo(origin: Context){
            val intent = Intent(origin, NewFavouriteActivity::class.java)
            origin.startActivity(intent)
        }
    }

    private val dependencies by lazy {
        application as GomokuDependenciesContainer
    }

    private val vm by viewModels<NewFavouriteViewModel> {
        NewFavouriteViewModel.factory(dependencies.gomokuServiceFirebase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            vm.favStateFlow.collect{
                if (it is Loaded && it.value.isSuccess){
                    finish()
                }
            }
        }

        setContent {
            val favFlow by vm.favStateFlow.collectAsState(initial = idle())
           GomokuEETheme {
               NewFavouriteScreen(
                   state = NewFavouriteState(
                       favInfo = favFlow,
                       title = vm.title,
                       opponent = vm.opponent
                   ),
                   onSaveRequest = { vm.saveFavourite(); FavouritesActivity.navigateTo(this) },
                   navigation = NavigationHandlers(
                       onBackRequested = { vm::dismiss ; finish() }
                   ),
                   onTitleChange = vm::changeTitle,
                   onOpponentChange = vm::changeOpponent,
                   onDismiss = { finish()}
               )
           }
        }
    }
}