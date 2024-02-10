package com.example.gomokuee.Screens.NewFavourite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gomokuee.Domain.FavInfo
import com.example.gomokuee.Domain.LoadState
import com.example.gomokuee.Domain.idle
import com.example.gomokuee.R
import com.example.gomokuee.Screens.Components.CustomBar
import com.example.gomokuee.Screens.Components.CustomOpponentTextFieldView
import com.example.gomokuee.Screens.Components.CustomTitleTextFieldView
import com.example.gomokuee.Screens.Components.NavigationHandlers
import com.example.gomokuee.Utils.BUTTON_RADIUS
import com.example.gomokuee.ui.theme.BackgroundBlue
import com.example.gomokuee.ui.theme.GomokuEETheme


data class NewFavouriteState(
    val favInfo : LoadState<FavInfo?> = idle(),
    val title : String = "",
    val opponent: String = "",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewFavouriteScreen(
    state : NewFavouriteState = NewFavouriteState(),
    onSaveRequest: () -> Unit = { },
    navigation : NavigationHandlers = NavigationHandlers(),
    onTitleChange: (String) -> Unit = { },
    onOpponentChange: (String) -> Unit = { },
    onDismiss: () -> Unit = { }
){
    Scaffold(
        topBar = { CustomBar(text = stringResource(id = R.string.new_favourite_screen_title), navigation = navigation) }
    ){ padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
           CustomTitleTextFieldView(
               value = state.title,
               label = stringResource(id = R.string.new_favourite_game_title),
               onClick = onTitleChange
           )
           CustomOpponentTextFieldView(
               value = state.opponent,
               label = stringResource(id = R.string.new_favourite_game_opponent),
               onClick = onOpponentChange
           )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = onSaveRequest,
                    enabled = isButtonEnabled(state),
                    shape = RoundedCornerShape(BUTTON_RADIUS),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 35.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.new_favourite_save),
                        color = BackgroundBlue
                    )
                }
                ElevatedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(BUTTON_RADIUS),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 35.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.new_favourite_dismiss),
                        color = BackgroundBlue
                    )
                }
            }

        }

    }
}


private fun isButtonEnabled(state: NewFavouriteState = NewFavouriteState()) =
    state.title.isNotBlank() && state.opponent.isNotBlank()

@Preview(showBackground = true)
@Composable
fun NewFavouritePreview(){
    GomokuEETheme {
        NewFavouriteScreen()
    }
}
