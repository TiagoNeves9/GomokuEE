package com.example.gomokuee.Screens.Favourites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.getOrNull
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.ui.theme.GomokuEETheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favourites : LoadState<List<FavInfo>>,
    onFavouriteSelected: (FavInfo) -> Unit,
    navigation: NavigationHandlers = NavigationHandlers()
){
    GomokuEETheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                CustomBar(
                    text = stringResource(id = R.string.activity_favourites_title),
                    navigation = navigation
                )
            }
        ) {
            padding ->
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {

                favourites.getOrNull()?.forEach { favInfo ->
                    FavouritesInfoView(favInfo = favInfo, onFavSelected = { onFavouriteSelected(favInfo) } )
                }
            }
        }
    }
}
