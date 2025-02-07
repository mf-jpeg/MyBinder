package pt.mf.mybinder.presentation.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import pt.mf.mybinder.presentation.search.HOLDER.TAG

/**
 * Created by Martim Ferreira on 07/02/2025
 */
object HOLDER {
    const val TAG = "CardSearchScreen"
}

@Composable
fun CardSearchScreen(padding: PaddingValues) {
    Text(text = TAG, modifier = Modifier
        .padding(padding)
        .fillMaxSize()
        .wrapContentSize(Alignment.Center))
}