package pt.mf.mybinder.utils

/**
 * Created by Martim Ferreira on 07/02/2025
 */
sealed class Screen(val route: String) {
    companion object {
        const val DECK_SELECT_SCREEN = "deck_select_screen"
        const val BINDER_SCREEN = "binder_screen"
        const val CARD_SEARCH_SCREEN = "card_search_screen"
        const val SETTINGS_SCREEN = "settings_screen"
    }

    data object DeckSelectScreen : Screen(DECK_SELECT_SCREEN)
    data object BinderScreen : Screen(BINDER_SCREEN)
    data object CardSearchScreen : Screen(CARD_SEARCH_SCREEN)
    data object SettingsScreen : Screen(SETTINGS_SCREEN)
}