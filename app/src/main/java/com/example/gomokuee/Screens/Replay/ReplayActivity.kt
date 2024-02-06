package com.example.gomokuee.Screens.Replay

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
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.FAVOURITE_EXTRA
import com.example.gomokuee.Screens.Common.FavExtra
import com.example.gomokuee.Screens.Common.getFavInfoExtra
import com.example.gomokuee.Screens.Common.toFavInfo
import com.example.gomokuee.ui.theme.GomokuEETheme
import kotlinx.coroutines.launch

class ReplayActivity : ComponentActivity() {

    companion object{
        fun navigateTo(origin: Context,favInfo: FavInfo){
            origin.startActivity(createIntent(origin, favInfo))
        }

        private fun createIntent(ctx: Context, favInfo: FavInfo): Intent {
            val intent = Intent(ctx, ReplayActivity::class.java)
            intent.putExtra(FAVOURITE_EXTRA, FavExtra(favInfo))
            return intent
        }
    }

    private val favInfoExtra: FavInfo by lazy {
        checkNotNull(getFavInfoExtra(intent)).toFavInfo()
    }

    private val dependencies by lazy {
        application as GomokuDependenciesContainer
    }

    private val viewModel by viewModels<ReplayScreenViewModel>{
        ReplayScreenViewModel.factory(dependencies.gomokuService)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.fetchFavourites()
            }
        }

        /*setContent {
            val currentFavourites by viewModel.favouritesList.collectAsState(initial = idle())
            GomokuEETheme {
                ReplayScreen(
                    favoriteGame = )
            }
        }*/

    }
}