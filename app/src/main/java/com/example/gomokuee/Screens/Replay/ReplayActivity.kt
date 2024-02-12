package com.example.gomokuee.Screens.Replay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Screens.Common.FAVOURITE_EXTRA
import com.example.gomokuee.Screens.Common.FavExtra
import com.example.gomokuee.Screens.Common.getFavInfoExtra
import com.example.gomokuee.Screens.Common.toFavInfo
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.ui.theme.GomokuEETheme

class ReplayActivity : ComponentActivity() {

    companion object{
        fun navigateTo(origin: Context, favInfo: FavExtra){
            origin.startActivity(createIntent(origin, favInfo))
        }

        private fun createIntent(ctx: Context, favInfo: FavExtra): Intent {
            val intent = Intent(ctx, ReplayActivity::class.java)
            intent.putExtra(FAVOURITE_EXTRA, favInfo)
            return intent
        }
    }

    private val favInfo: FavInfo by lazy {
        checkNotNull(getFavInfoExtra(intent)).toFavInfo()
    }

    private val viewModel by viewModels<ReplayScreenViewModel>{
        ReplayScreenViewModel.factory(favInfo.plays)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GomokuEETheme {
                ReplayScreen(
                    favInfo = favInfo,
                    index = viewModel.index,
                    replayHandlers = ReplayHandlers( viewModel::next, viewModel::prev),
                    navigation = NavigationHandlers(onBackRequested = { finish() })
                )
            }
        }

    }
}