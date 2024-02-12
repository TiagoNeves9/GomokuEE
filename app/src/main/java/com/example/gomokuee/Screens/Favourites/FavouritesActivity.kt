package com.example.gomokuee.Screens.Favourites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.FAVOURITE_EXTRA
import com.example.gomokuee.Screens.Common.FavExtra
import com.example.gomokuee.Screens.Common.USER_INFO_EXTRA
import com.example.gomokuee.Screens.Common.UserInfoExtra
import com.example.gomokuee.Screens.Common.getFavInfoExtra
import com.example.gomokuee.Screens.Common.getUserInfoExtra
import com.example.gomokuee.Screens.Common.toFavInfo
import com.example.gomokuee.Screens.Common.toUserInfo
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Replay.ReplayActivity
import kotlinx.coroutines.launch

class FavouritesActivity : ComponentActivity() {
    companion object{

        fun createIntent(ctx: Context): Intent{
            val intent = Intent(ctx, FavouritesActivity::class.java)
            return intent
        }
        fun navigateTo(ctx: Context){
            ctx.startActivity(createIntent(ctx))
        }
    }

    private val dependecies by lazy {
        application as GomokuDependenciesContainer
    }


    private val viewModel by viewModels<FavouritesViewModel>{
        FavouritesViewModel.factory(dependecies.gomokuServiceFirebase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.fetchFavourites()
        }

        setContent {
            val currentFavourites by viewModel.favourites.collectAsState(initial = idle())
            FavouritesScreen(
                favourites = currentFavourites,
                onFavouriteSelected = {favinfo -> ReplayActivity.navigateTo(this, FavExtra(favinfo)) },
                navigation = NavigationHandlers(
                    onBackRequested = {finish()}
                )
            )
        }
    }
}