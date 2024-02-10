package com.example.gomokuee

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.gomokuee.Domain.UserInfoRepository
import com.example.gomokuee.Service.FakeGomokuService
import com.example.gomokuee.Service.GomokuService
import com.example.gomokuee.Storage.UserInfoDataStore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MemoryCacheSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


interface GomokuDependenciesContainer {
    val gomokuService : GomokuService

    val userInfoRepository : UserInfoRepository
}

class GomokuApp : Application(), GomokuDependenciesContainer{
    private val dataStore : DataStore<Preferences> by preferencesDataStore(name = "user_info")

    private val emulatedFirestoreDb: FirebaseFirestore by lazy {
        Firebase.firestore.also {
            it.useEmulator("10.0.2.2", 8080)
            it.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(MemoryCacheSettings.newBuilder().build())
                .build()
        }
    }

    override val gomokuService: GomokuService = FakeGomokuService()

    override val userInfoRepository: UserInfoRepository
        get() = UserInfoDataStore(dataStore)
}