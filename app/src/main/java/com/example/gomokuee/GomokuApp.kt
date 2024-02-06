package com.example.gomokuee

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gomokuee.Domain.UserInfoRepository
import com.example.gomokuee.Service.FakeGomokuService
import com.example.gomokuee.Service.GomokuService
import com.example.gomokuee.Storage.UserInfoDataStore


interface GomokuDependenciesContainer {
    val gomokuService : GomokuService

    val userInfoRepository : UserInfoRepository
}

class GomokuApp : Application(), GomokuDependenciesContainer{
    private val dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_info")

    override val gomokuService: GomokuService = FakeGomokuService()

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)
}