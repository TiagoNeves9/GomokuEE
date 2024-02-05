package com.example.gomokuee.Screens.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.GomokuDependenciesContainer
import com.example.gomokuee.Screens.Common.USER_INFO_EXTRA
import com.example.gomokuee.Screens.Common.UserInfoExtra
import com.example.gomokuee.Screens.Common.getUserInfoExtra
import com.example.gomokuee.Screens.Common.toUserInfo
import com.example.gomokuee.Screens.Game.GameActivity
import com.example.gomokuee.ui.theme.GomokuEETheme

class HomeActivity: ComponentActivity() {

    companion object {

        private fun createIntent(ctx: Context, userInfo: UserInfo): Intent{
            val intent = Intent(ctx, HomeActivity::class.java)
            intent.putExtra(USER_INFO_EXTRA, UserInfoExtra(userInfo))
            return intent
        }
        fun navigateTo(origin: Context, userInfo: UserInfo){
            origin.startActivity(createIntent(origin,userInfo))
        }
    }

    private val userInfoExtra: UserInfo by lazy {
        checkNotNull(getUserInfoExtra(intent)).toUserInfo()
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
                    userInfo = userInfoExtra,
                    error = viewModel.error,
                    onFavoritesRequest = {},
                    onPlayRequest = {GameActivity.navigateTo(this)},
                    onDismissError = viewModel::onDismissError
                )
            }

        }
    }


}