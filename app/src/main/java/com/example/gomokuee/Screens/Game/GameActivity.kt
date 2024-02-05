package com.example.gomokuee.Screens.Game

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.gomokuee.Domain.Game
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.getGameExtra
import com.example.gomokuee.Screens.Common.getUserInfoExtra
import com.example.gomokuee.Screens.Common.toUserInfo
import kotlinx.coroutines.launch


class GameActivity : ComponentActivity() {

    private val userInfoExtra: UserInfo by lazy {
        checkNotNull(getUserInfoExtra(intent)).toUserInfo()
    }

    private val gameExtra: Game by lazy {
        checkNotNull(getGameExtra(intent)).toGame()
    }

    private val dependencies by lazy { application as GomokuDependenciesContainer }

    private val viewModel by viewModels<GameScreenViewModel> {
        GameScreenViewModel.factory(dependencies.gomokuService, gameExtra, userInfoExtra)
    }

    companion object {
        fun navigateTo(origin: ComponentActivity){
            val intent = Intent(origin, GameActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

            }
        }
    }
}