package pt.mf.mybinder.presentation.container

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import pt.mf.mybinder.R
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Screen.BinderScreen
import pt.mf.mybinder.utils.Screen.CardSearchScreen
import pt.mf.mybinder.utils.Screen.DeckSelectScreen
import pt.mf.mybinder.utils.Screen.SettingsScreen
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
        navController.navigate(DeckSelectScreen)
    }

    fun handleBinderOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to BinderScreen.")
        Utils.tactileFeedback()
        navController.navigate(BinderScreen)
    }

    fun handleSearchOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to SearchScreen.")
        Utils.tactileFeedback()
        navController.navigate(CardSearchScreen)
    }

    fun handleSettingsOption(navController: NavController) {
        Logger.debug(TAG, "Navigating to SettingsScreen.")
        Utils.tactileFeedback()
        navController.navigate(SettingsScreen)
    }

    fun getScreenName(route: String?): String {
        if (route == null)
            return Utils.getString(R.string.screen)

        val index = route.lastIndexOf(".")

        if (index == -1)
            return Utils.getString(R.string.screen)

        val name = route.substring(index + 1)

        return when (name) {
            DeckSelectScreen.javaClass.simpleName -> Utils.getString(R.string.top_bar_deck)
            BinderScreen.javaClass.simpleName -> Utils.getString(R.string.top_bar_binder)
            CardSearchScreen.javaClass.simpleName -> Utils.getString(R.string.top_bar_search)
            SettingsScreen.javaClass.simpleName -> Utils.getString(R.string.top_bar_settings)
            else -> Utils.getString(R.string.screen)
        }
    }
}