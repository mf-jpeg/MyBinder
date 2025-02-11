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
import pt.mf.mybinder.utils.Screen.BinderScreen
import pt.mf.mybinder.utils.Screen.CardSearchScreen
import pt.mf.mybinder.utils.Screen.DeckSelectScreen
import pt.mf.mybinder.utils.Screen.SettingsScreen

/**
 * Created by Martim Ferreira on 07/02/2025
 */
@Composable
fun Navigation(padding: PaddingValues, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = DeckSelectScreen
    ) {
        composable<DeckSelectScreen> {
            DeckSelectScreen(padding)
        }

        composable<BinderScreen> {
            BinderScreen(padding)
        }

        composable<CardSearchScreen> {
            CardSearchScreen(padding, navController)
        }

        composable<SettingsScreen> {
            SettingsScreen(padding)
        }
    }
}