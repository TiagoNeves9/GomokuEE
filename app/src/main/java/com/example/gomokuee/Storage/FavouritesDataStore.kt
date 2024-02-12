package com.example.gomokuee.Storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences


private const val TITLE = "Title"
private const val OPPONENT = "Opponent"
private const val DATE = "Date"
private const val PLAYS = "Plays"
private const val ID = "Id"
class FavouritesDataStore(private val store: DataStore<Preferences>) {
}