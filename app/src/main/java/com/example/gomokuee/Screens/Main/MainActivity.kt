package com.example.gomokuee.Screens.Main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.gomokuee.Domain.UserInfo
import com.example.gomokuee.Screens.Game.GameActivity
import com.example.gomokuee.Screens.Home.HomeActivity
import com.example.gomokuee.ui.theme.GomokuEETheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainScreenViewModel>()

    companion object {
        fun navigateTo(origin: ComponentActivity){
            val intent = Intent(origin, MainActivity::class.java)
            origin.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GomokuEETheme {
                MainScreen(
                    onStartEnable = true,
                    onStartRequest = { HomeActivity.navigateTo(this, userInfo = UserInfo("1","Tiago","abc123")) }
                )
            }
        }
    }
}