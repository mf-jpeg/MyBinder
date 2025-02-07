package pt.mf.mybinder.presentation.search

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class CardSearchViewModel : ViewModel() {
    companion object {
        const val TAG = "CardSearchViewModel"
    }

    data class CardSearchViewState(
        var counter: Int
    )

    private val _viewState = MutableStateFlow(CardSearchViewState(0))
    val viewState = _viewState.asStateFlow()

    fun increment() {
        val current = _viewState.value.counter
        _viewState.value = _viewState.value.copy(counter = current + 1)
    }

}