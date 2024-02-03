package com.example.gomokuee.Screens.Main

import androidx.core.app.ComponentActivity
import com.example.gomokuee.GomokuDependenciesContainer

class MainActivity : ComponentActivity() {
    private val dependencies by lazy {
        application as GomokuDependenciesContainer
    }

    private val viewModel by viewModels<MainScreenViewModel>{

    }
}