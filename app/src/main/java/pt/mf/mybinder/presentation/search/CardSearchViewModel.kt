package pt.mf.mybinder.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.mf.mybinder.data.repository.CardRepositoryImpl
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Result
import pt.mf.mybinder.utils.Utils

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class CardSearchViewModel : ViewModel() {
    companion object {
        const val TAG = "CardSearchViewModel"
    }

    data class CardSearchViewState(
        val isLoading: Boolean = false
    )

    private val _viewState = MutableStateFlow(CardSearchViewState())
    val viewState = _viewState.asStateFlow()

    private val cardRepository = CardRepositoryImpl()

    fun searchCard(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            modifyLoadingState(isLoading = true)

            when (val result = cardRepository.searchCard(name)) {
                is Result.Success -> {
                    Logger.debug(TAG, "Request success!")
                    Utils.tactileFeedback()
                    modifyLoadingState(isLoading = false)
                }

                is Result.Error -> {
                    Logger.debug(TAG, "Request error! ${result.exception.message}")
                    Utils.tactileFeedback()
                    modifyLoadingState(isLoading = false)
                }
            }
        }
    }

    private fun modifyLoadingState(isLoading: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }
}