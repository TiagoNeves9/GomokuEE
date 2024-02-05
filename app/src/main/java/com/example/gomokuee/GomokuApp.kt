package com.example.gomokuee

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.gomokuee.Domain.UserInfoRepository
import com.example.gomokuee.Service.FakeGomokuService
import com.example.gomokuee.Service.GomokuService



interface GomokuDependenciesContainer {
    val gomokuService : GomokuService

    val userInfoRepository : UserInfoRepository
}

class GomokuApp : Application(), GomokuDependenciesContainer{

    override val gomokuService: GomokuService = FakeGomokuService()

    override val userInfoRepository: UserInfoRepository
        get() = TODO("Not yet implemented")
}