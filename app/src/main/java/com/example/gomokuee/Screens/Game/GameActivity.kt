package com.example.gomokuee.Screens.Game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gomokuee.Domain.Board.BoardDraw
import com.example.gomokuee.Domain.Board.BoardWin
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.Loaded
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Domain.getOrNull
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.Domain.loaded
import com.example.gomokuee.Domain.success
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.GAME_EXTRA
import com.example.gomokuee.Screens.Common.GameExtra
import com.example.gomokuee.Screens.Common.USER_INFO_EXTRA
import com.example.gomokuee.Screens.Common.UserInfoExtra
import com.example.gomokuee.Screens.Common.getGameExtra
import com.example.gomokuee.Screens.Common.getUserInfoExtra
import com.example.gomokuee.Screens.Common.toUserInfo
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.Home.HomeActivity
import com.example.gomokuee.ui.theme.GomokuEETheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class GameActivity : ComponentActivity() {

    private val gameExtra: Game by lazy {
        checkNotNull(getGameExtra(intent)).toGame()
    }

    private val dependencies by lazy { application as GomokuDependenciesContainer }

    private val viewModel by viewModels<GameScreenViewModel> {
        GameScreenViewModel.factory(dependencies.gomokuService, gameExtra)
    }

    companion object {
        fun navigateTo(origin: Context, game: GameExtra){
            origin.startActivity(createIntent(origin,game))
        }

        private fun createIntent(ctx: Context, game: GameExtra) : Intent{
            val intent = Intent(ctx, GameActivity::class.java)
            intent.putExtra(GAME_EXTRA, game)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }*/

        //viewModel.fetchGame()
        setContent {
            val currentGame by viewModel.currentGameFlow.collectAsState(initial = idle())
            GomokuEETheme {
                GameScreen(
                    currentGame = currentGame,
                    selectedCell = viewModel.selectedCell,
                    onPlayRequested = viewModel::play,
                    onCellSelected = viewModel::changeSelectedCell,
                    onDismissError = viewModel::resetCurrentGameFlowFlowToIdle,
                    navigation = NavigationHandlers(
                        onBackRequested = { finish() },
                        onFavouriteRequested = { }
                    )
                )
            }
        }
    }



    private fun doNavigation(game: Game?) {
        if (game != null && (game.board is BoardWin || game.board is BoardDraw)) {
            HomeActivity.navigateTo(origin = this)
            finish()
        }
    }
}