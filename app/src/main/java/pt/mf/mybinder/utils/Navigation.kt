package pt.mf.mybinder.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pt.mf.mybinder.presentation.binder.BinderScreen
import pt.mf.mybinder.presentation.deck.DeckSelectScreen
import pt.mf.mybinder.presentation.search.CardSearchScreen
import pt.mf.mybinder.presentation.settings.SettingsScreen

/**
 * Created by Martim Ferreira on 07/02/2025
 */
@Composable
fun Navigation(padding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.CardSearchScreen.route
    ) {
        composable(route = Screen.DeckSelectScreen.route) {
            DeckSelectScreen(padding)
        }

        composable(route = Screen.BinderScreen.route) {
            BinderScreen(padding)
        }

        composable(route = Screen.CardSearchScreen.route) {
            CardSearchScreen(padding)
        }

        composable(route = Screen.SettingsScreen.route) {
            SettingsScreen(padding)
        }
    }
}