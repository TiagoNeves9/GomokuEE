package com.example.gomokuee.Screens.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gomokuee.Domain.Board.BOARD_DIM
import com.example.gomokuee.Domain.Board.createBoard
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.Opening
import com.example.gomokuee.Domain.Player
import com.example.gomokuee.Domain.Rules
import com.example.gomokuee.Domain.Turn
import com.example.gomokuee.Domain.User
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Domain.Variant
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.FAVOURITE_EXTRA
import com.example.gomokuee.Screens.Common.FavExtra
import com.example.gomokuee.Screens.Common.GameExtra
import com.example.gomokuee.Screens.Common.USER_INFO_EXTRA
import com.example.gomokuee.Screens.Common.UserInfoExtra
import com.example.gomokuee.Screens.Common.getFavInfoExtra
import com.example.gomokuee.Screens.Common.getUserInfoExtra
import com.example.gomokuee.Screens.Common.toFavInfo
import com.example.gomokuee.Screens.Common.toUserInfo
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Favourites.FavouritesActivity
import com.example.gomokuee.Screens.Game.GameActivity
import com.example.gomokuee.Screens.Replay.ReplayActivity
import com.example.gomokuee.Service.FakeGomokuService
import com.example.gomokuee.Service.GomokuGames
import com.example.gomokuee.ui.theme.GomokuEETheme

class HomeActivity: ComponentActivity() {

    companion object {

        private fun createIntent(ctx: Context): Intent{
            val intent = Intent(ctx, HomeActivity::class.java)
            //intent.putExtra(USER_INFO_EXTRA, UserInfoExtra(userInfo))
            //intent.putExtra(FAVOURITE_EXTRA,FavExtra(favInfo))
            return intent
        }
        fun navigateTo(origin: Context){
            origin.startActivity(createIntent(origin))
        }
    }

    /*private val userInfoExtra: UserInfo by lazy {
        checkNotNull(getUserInfoExtra(intent)).toUserInfo()
    }*/

    private val favInfoExtra: FavInfo by lazy {
        checkNotNull(getFavInfoExtra(intent)).toFavInfo()
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
                    //userInfo = userInfoExtra,
                    error = viewModel.error,
                    onFavoritesRequest = { ReplayActivity.navigateTo(this) },
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