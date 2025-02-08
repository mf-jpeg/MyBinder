package pt.mf.mybinder.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Created by Martim Ferreira on 07/02/2025
 */
object HOLDER {
    const val TAG = "CardSearchScreen"
}

@Composable
fun CardSearchScreen(padding: PaddingValues) {
    val viewModel: CardSearchViewModel = viewModel()
    val viewState by viewModel.viewState.collectAsState()

    Column {
        if (viewState.isLoading)
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            )

        Button(
            onClick = { viewModel.searchCard("charizard") },
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        ) {
            Text(text = "Click me")
        }
    }
}