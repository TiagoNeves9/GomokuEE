package com.example.gomokuee.Screens.Favourites

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.ui.theme.GomokuEETheme

@Composable
fun FavouritesInfoView(
    favInfo: FavInfo,
    onFavSelected: (FavInfo) -> Unit
){
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 3.dp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFavSelected(favInfo) }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = favInfo.title)
            Text(text = favInfo.opponent)
            Text(text = favInfo.date)
        }
    }
}

@Preview(showBackground = true,  showSystemUi = true)
@Composable
fun FavPreview(){
    GomokuEETheme {
        FavouritesInfoView(
            favInfo = FavInfo("Game1","Antunes","05-02-2024 11:45"),
            onFavSelected = { }
        )
    }
}