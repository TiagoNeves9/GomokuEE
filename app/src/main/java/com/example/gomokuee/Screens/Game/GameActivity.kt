package com.example.gomokuee.Screens.Game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.GAME_EXTRA
import com.example.gomokuee.Screens.Common.GameExtra
import com.example.gomokuee.Screens.Common.getGameExtra
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Screens.NewFavourite.NewFavouriteActivity
import com.example.gomokuee.ui.theme.GomokuEETheme



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
        viewModel.startTimer()

        setContent {
            val currentGame by viewModel.currentGameFlow.collectAsState(initial = idle())
            GomokuEETheme {
                GameScreen(
                    currentGame = currentGame,
                    selectedCell = viewModel.selectedCell,
                    time = viewModel.time,
                    onPlayRequested = viewModel::play,
                    onCellSelected = viewModel::changeSelectedCell,
                    onDismissError = viewModel::resetCurrentGameFlowFlowToIdle,
                    onFavouriteRequest =  { NewFavouriteActivity.navigateTo(this) },
                    navigation = NavigationHandlers(
                        onBackRequested = { finish() }
                    ),
                )
            }
        }
    }
}