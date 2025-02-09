package pt.mf.mybinder.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.repository.CardRepositoryImpl
import pt.mf.mybinder.data.repository.SetRepositoryImpl
import pt.mf.mybinder.data.repository.SubtypeRepositoryImpl
import pt.mf.mybinder.domain.model.remote.Card
import pt.mf.mybinder.domain.usecase.CardUseCase
import pt.mf.mybinder.domain.usecase.SetUseCase
import pt.mf.mybinder.domain.usecase.SubtypeUseCase
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Preferences
import pt.mf.mybinder.utils.Preferences.SETS_READY_KEY
import pt.mf.mybinder.utils.Preferences.SUBTYPES_READY_KEY
import pt.mf.mybinder.utils.Result
import pt.mf.mybinder.utils.Utils
import pt.mf.mybinder.utils.Utils.empty

/**
 * Created by Martim Ferreira on 07/02/2025
 */
class CardSearchViewModel : ViewModel() {
    companion object {
        const val TAG = "CardSearchViewModel"
    }

    data class CardSearchViewState(
        val isLoading: Boolean = false,
        val isFilterWindowVisible: Boolean = false,
        val cards: List<Card> = listOf(),
        val selectedCardId: String = String.empty(),
        val subtypes: List<Subtype> = listOf(),
        val sets: List<Set> = listOf(),
        val selectOrderIndex: Int = 0
    )

    private val _viewState = MutableStateFlow(CardSearchViewState())
    val viewState = _viewState.asStateFlow()

    private val cardUseCase = CardUseCase(CardRepositoryImpl())
    private val subtypeUseCase = SubtypeUseCase(SubtypeRepositoryImpl())
    private val setUseCase = SetUseCase(SetRepositoryImpl())

    init {
        fetchLocalSubtypes()
        fetchLocalSets()
    }

    fun searchCard(name: String) {
        Logger.debug(TAG, "Searching for card with title \"$name\".")

        viewModelScope.launch(Dispatchers.IO) {
            modifyLoadingVisibility(isLoading = true)

            when (val result = cardUseCase.searchCard(name)) {
                is Result.Success -> {
                    Utils.tactileFeedback()
                    populateCardList(result.data.data)
                    modifyLoadingVisibility(isLoading = false)
                }

                is Result.Error -> {
                    Utils.tactileFeedback()
                    modifyLoadingVisibility(isLoading = false)
                }
            }
        }
    }

    private fun fetchLocalSubtypes() {
        modifyLoadingVisibility(isLoading = true)

        viewModelScope.launch {
            subtypeUseCase.fetchLocalSubtypes().collect {
                _viewState.value = _viewState.value.copy(subtypes = it)
                modifyLoadingVisibility(isLoading = false)
            }
        }
    }

    private fun fetchLocalSets() {
        modifyLoadingVisibility(isLoading = true)

        viewModelScope.launch {
            setUseCase.fetchLocalSets().collect {
                _viewState.value = _viewState.value.copy(sets = it)
                modifyLoadingVisibility(isLoading = false)
            }
        }
    }

    fun isFilterReady(): Boolean {
        return Preferences.getPref<Boolean>(SETS_READY_KEY) &&
                Preferences.getPref<Boolean>(SUBTYPES_READY_KEY)
    }

    // TODO
    fun applyFilters() {
        Logger.debug(TAG, "Applying filters.")
    }

    private fun modifyLoadingVisibility(isLoading: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }

    fun modifyFilterWindowVisibility(isVisibile: Boolean) {
        _viewState.value = _viewState.value.copy(isFilterWindowVisible = isVisibile)
    }

    private fun populateCardList(cards: List<Card>) {
        _viewState.value = _viewState.value.copy(cards = cards)
    }

    fun clearCardList() {
        _viewState.value = _viewState.value.copy(cards = listOf())
    }

    fun modifySelectedCardId(id: String) {
        _viewState.value = _viewState.value.copy(selectedCardId = id)
    }

    fun clearClickedCardId() {
        _viewState.value = _viewState.value.copy(selectedCardId = String.empty())
    }

    fun modifySelectedOrderIndex(index: Int) {
        _viewState.value = _viewState.value.copy(selectOrderIndex = index)
    }
}