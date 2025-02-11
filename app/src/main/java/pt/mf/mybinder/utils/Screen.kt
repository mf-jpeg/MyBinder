package pt.mf.mybinder.utils

import kotlinx.serialization.Serializable

/**
 * Created by Martim Ferreira on 07/02/2025
 */
@Serializable
sealed class Screen {
    @Serializable
    data object DeckSelectScreen : Screen()

    @Serializable
    data object BinderScreen : Screen()

    @Serializable
    data object CardSearchScreen : Screen()

    @Serializable
    data object SettingsScreen : Screen()
}