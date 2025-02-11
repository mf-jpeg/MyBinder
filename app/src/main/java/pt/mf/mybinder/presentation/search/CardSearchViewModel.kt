package pt.mf.mybinder.presentation.search

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import pt.mf.mybinder.R
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.repository.SearchRepositoryImpl
import pt.mf.mybinder.data.repository.SetRepositoryImpl
import pt.mf.mybinder.data.repository.SubtypeRepositoryImpl
import pt.mf.mybinder.domain.model.remote.Card
import pt.mf.mybinder.domain.usecase.SearchUseCase
import pt.mf.mybinder.domain.usecase.SetUseCase
import pt.mf.mybinder.domain.usecase.SubtypeUseCase
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.MyBinder
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_ORDER_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SETS_READY_KEY
import pt.mf.mybinder.utils.PreferencesManager.SUBTYPES_READY_KEY
import pt.mf.mybinder.utils.PreferencesManager.getPref
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
        val nameInLastPerformedQuery: String = String.empty(),
        val currentResultPage: Int = 1,
        val cardTotalCount: Int = 0,
        val selectedCardId: String = String.empty(),
        val subtypes: List<Subtype> = listOf(),
        val sets: List<Set> = listOf(),
        val selectedSubtype: String = String.empty(),
        val selectedSet: String = String.empty(),
        val selectedOrder: Int = 0,
        val isSubtypeFilterEnabled: Int = 0,
        val isSetFilterEnabled: Int = 0,

        val isNothingToDisplay: Boolean = true,
        val isNoResultsFound: Boolean = false,
        val isRequestError: Boolean = false
    )

    private val _viewState = MutableStateFlow(CardSearchViewState())
    val viewState = _viewState.asStateFlow()

    private val searchUseCase = SearchUseCase(SearchRepositoryImpl())
    private val subtypeUseCase = SubtypeUseCase(SubtypeRepositoryImpl())
    private val setUseCase = SetUseCase(SetRepositoryImpl())

    init {
        fetchLocalSubtypes()
        fetchLocalSets()
    }

    fun fetchCards(name: String) {
        Logger.debug(TAG, "Searching for card with title \"$name\".")

        viewModelScope.launch(Dispatchers.IO) {
            setLoadingVisibility(isLoading = true)
            recallSelectedFilters()

            val setId =
                setUseCase.fetchLocalSetIdByName(getPref(SEARCH_SET_KEY)).firstOrNull()

            val result = searchUseCase.fetchCards(
                name = name,
                subtype = getPref(SEARCH_SUBTYPE_KEY),
                setId = setId.orEmpty(),
                isSubtypeFilterEnabled = Utils.intToBool(_viewState.value.isSubtypeFilterEnabled),
                isSetFilterEnabled = Utils.intToBool(_viewState.value.isSetFilterEnabled),
                pageNumber = viewState.value.currentResultPage,
                selectedOrder = _viewState.value.selectedOrder
            )

            when (result) {
                is Result.Success -> {
                    Utils.tactileFeedback()

                    if (result.data.data.isNotEmpty()) {
                        _viewState.value = _viewState.value.copy(
                            cardTotalCount = result.data.totalCount
                        )
                        appendCardList(result.data.data)
                    } else
                        changeNoResultsFoundVisibility(isNoResultsFound = true)

                    changeRequestErrorVisibility(isRequestError = false)
                    setLoadingVisibility(isLoading = false)
                }

                is Result.Error -> {
                    Utils.tactileFeedback()
                    changeRequestErrorVisibility(isRequestError = true)
                    setLoadingVisibility(isLoading = false)
                }
            }
        }
    }

    private fun fetchLocalSubtypes() {
        setLoadingVisibility(isLoading = true)

        viewModelScope.launch {
            subtypeUseCase.fetchLocalSubtypes().collect {
                _viewState.value = _viewState.value.copy(subtypes = it)
                setLoadingVisibility(isLoading = false)
            }
        }
    }

    private fun fetchLocalSets() {
        setLoadingVisibility(isLoading = true)

        viewModelScope.launch {
            setUseCase.fetchLocalSets().collect {
                _viewState.value = _viewState.value.copy(sets = it)
                setLoadingVisibility(isLoading = false)
            }
        }
    }

    fun isFilterReady(): Boolean {
        return getPref<Boolean>(SETS_READY_KEY) &&
                getPref<Boolean>(SUBTYPES_READY_KEY)
    }

    fun applyFilters() {
        searchUseCase.applyFilters(
            _viewState.value.selectedSubtype,
            _viewState.value.selectedSet,
            _viewState.value.selectedOrder,
            _viewState.value.isSubtypeFilterEnabled,
            _viewState.value.isSetFilterEnabled
        )
    }

    private fun setLoadingVisibility(isLoading: Boolean) {
        _viewState.value = _viewState.value.copy(isLoading = isLoading)
    }

    fun setFilterWindowVisibility(isVisibile: Boolean) {
        _viewState.value = _viewState.value.copy(isFilterWindowVisible = isVisibile)
    }

    private fun appendCardList(cards: List<Card>) {
        _viewState.value = _viewState.value.copy(
            cards = mutableListOf<Card>().also {
                it.addAll(_viewState.value.cards)
                it.addAll(cards)
            })
    }

    fun clearCardList() {
        _viewState.value = _viewState.value.copy(cards = listOf())
    }

    fun fetchNextPage() {
        if (_viewState.value.cards.size == _viewState.value.cardTotalCount) {
            Logger.debug(TAG, "All cards have already been fetched.")
            return
        }

        Logger.debug(TAG, "Fetching next page.")
        incrementCurrentResultPage()
        fetchCards(viewState.value.nameInLastPerformedQuery)
    }

    private fun incrementCurrentResultPage() {
        _viewState.value = _viewState.value.copy(
            currentResultPage = _viewState.value.currentResultPage + 1
        )
    }

    fun resetCurrentResultPage() {
        _viewState.value = _viewState.value.copy(currentResultPage = 1)
    }

    private fun resetCardTotalCount() {
        _viewState.value = _viewState.value.copy(cardTotalCount = 0)
    }

    fun changeNameInLastPerformedQuery(name: String) {
        _viewState.value = _viewState.value.copy(nameInLastPerformedQuery = name)
    }

    fun setSelectedCardId(id: String) {
        _viewState.value = _viewState.value.copy(selectedCardId = id)
    }

    fun clearClickedCardId() {
        _viewState.value = _viewState.value.copy(selectedCardId = String.empty())
    }

    fun changeIsNothingToDisplayVisibility(isNothingToDisplay: Boolean) {
        _viewState.value = _viewState.value.copy(isNothingToDisplay = isNothingToDisplay)
    }

    fun changeNoResultsFoundVisibility(isNoResultsFound: Boolean) {
        _viewState.value = _viewState.value.copy(isNoResultsFound = isNoResultsFound)
    }

    fun changeRequestErrorVisibility(isRequestError: Boolean) {
        _viewState.value = _viewState.value.copy(isRequestError = isRequestError)
    }

    fun getSelectedSubtype(): String {
        return _viewState.value.selectedSubtype
    }

    fun setSelectedSubtype(selectedSubtype: String) {
        _viewState.value = _viewState.value.copy(selectedSubtype = selectedSubtype)
    }

    fun getSelectedSet(): String {
        return _viewState.value.selectedSet
    }

    fun setSelectedSet(selectedSet: String) {
        _viewState.value = _viewState.value.copy(selectedSet = selectedSet)
    }

    fun getSelectedOrder(): Int {
        return _viewState.value.selectedOrder
    }

    fun setSelectedOrder(index: Int) {
        _viewState.value = _viewState.value.copy(selectedOrder = index)
    }

    fun isSubtypeFilterEnabled(): Int {
        return _viewState.value.isSubtypeFilterEnabled
    }

    fun setSubtypeFilterEnabled(isEnabled: Int) {
        _viewState.value = _viewState.value.copy(isSubtypeFilterEnabled = isEnabled)
    }

    fun isSetFilterEnabled(): Int {
        return _viewState.value.isSetFilterEnabled
    }

    fun setSetFilterEnabled(isEnabled: Int) {
        _viewState.value = _viewState.value.copy(isSetFilterEnabled = isEnabled)
    }

    fun recallSelectedFilters() {
        Logger.debug(TAG, "Recalling selected filters.")

        val selectedSubtype = getPref<String>(SEARCH_SUBTYPE_KEY).ifEmpty {
            _viewState.value.subtypes.first().name
        }

        val selectedSet = getPref<String>(SEARCH_SET_KEY).ifEmpty {
            _viewState.value.sets.first().name
        }

        _viewState.value = _viewState.value.copy(
            selectedSubtype = selectedSubtype,
            selectedSet = selectedSet,
            selectedOrder = getPref(SEARCH_ORDER_KEY),
            isSubtypeFilterEnabled = getPref<Int>(SEARCH_SUBTYPE_ACTIVE_KEY),
            isSetFilterEnabled = getPref<Int>(SEARCH_SET_ACTIVE_KEY),
        )
    }

    fun formatPrice(price: Float?): String {
        val lowest = Utils.getString(R.string.lowest)
        return if (price != null) "$lowest: $price€" else "$lowest: N/A"
    }

    fun openWebView(url: String) {
        Logger.debug(TAG, "Emitting WebView intent.")

        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }.also {
            MyBinder.ctx.startActivity(it)
        }
    }
}