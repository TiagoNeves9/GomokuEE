package com.example.gomokuee.Screens.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.GameExtra
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Favourites.FavouritesActivity
import com.example.gomokuee.Screens.Game.GameActivity
import com.example.gomokuee.Service.GomokuGames
import com.example.gomokuee.ui.theme.GomokuEETheme

class HomeActivity: ComponentActivity() {

    companion object {

        private fun createIntent(ctx: Context): Intent{
            val intent = Intent(ctx, HomeActivity::class.java)
            return intent
        }
        fun navigateTo(origin: Context){
            origin.startActivity(createIntent(origin))
        }
    }

    private val dependencies by lazy {
        application as GomokuDependenciesContainer
    }

    private val viewModel by viewModels<HomeViewModel>{
        HomeViewModel.factory(dependencies.userInfoRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GomokuEETheme {
                HomeScreen(
                    error = viewModel.error,
                    onFavoritesRequest = { FavouritesActivity.navigateTo(this)},
                    onPlayRequest = { GameActivity.navigateTo(this,GameExtra(GomokuGames.createGame()))},
                    onDismissError = viewModel::onDismissError,
                    navigation = NavigationHandlers(
                        onBackRequested = { finish() }
                    )
                )
            }

        }
    }


}