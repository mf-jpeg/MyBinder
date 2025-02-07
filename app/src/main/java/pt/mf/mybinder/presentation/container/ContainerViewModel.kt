package pt.mf.mybinder.presentation.container

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Screen
import pt.mf.mybinder.utils.Utils

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class ContainerViewModel : ViewModel() {
    companion object {
        const val TAG = "ContainerViewModel"
    }

    fun handleDeckOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to DeckSelectScreen.")
        Utils.tactileFeedback()
        navController.navigate(Screen.DECK_SELECT_SCREEN)
    }

    fun handleBinderOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to BinderScreen.")
        Utils.tactileFeedback()
        navController.navigate(Screen.BINDER_SCREEN)
    }

    fun handleSearchOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to SearchScreen.")
        Utils.tactileFeedback()
        navController.navigate(Screen.CARD_SEARCH_SCREEN)
    }

    fun handleSettingsOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to SettingsScreen.")
        Utils.tactileFeedback()
        navController.navigate(Screen.SETTINGS_SCREEN)
    }
}