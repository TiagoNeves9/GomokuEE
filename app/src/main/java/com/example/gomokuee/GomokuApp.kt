package com.example.gomokuee

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.gomokuee.Service.GomokuService



interface GomokuDependenciesContainer {
    val gomokuService : GomokuService
}

class GomokuApp : Application(), GomokuDependenciesContainer {
    private val dataStore: DataStore<Preferences> by
}