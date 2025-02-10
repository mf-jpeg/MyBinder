package pt.mf.mybinder.domain.usecase

import pt.mf.mybinder.data.model.remote.CardDetailsResponse
import pt.mf.mybinder.data.model.remote.CardSearchResponse
import pt.mf.mybinder.domain.repository.CardRepository
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_ORDER_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_KEY
import pt.mf.mybinder.utils.PreferencesManager.setPref
import pt.mf.mybinder.utils.Result

/**
 * Created by Martim Ferreira on 10/02/2025
 */
class SearchUseCase(private val repository: CardRepository) : BaseUseCase() {
    private companion object {
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_PAGE_NUMBER = 1
    }

    suspend fun searchCard(
        name: String,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
    ): Result<CardSearchResponse> {
        return performHttpRequest {
            repository.searchCard(name, pageSize, pageNumber)
        }
    }

    suspend fun fetchCardDetails(
        id: String
    ): Result<CardDetailsResponse> {
        return performHttpRequest {
            repository.fetchCardDetails(id)
        }
    }

    fun applyFilters(
        selectedSubtype: String,
        selectedSet: String,
        selectedOrder: Int,
        isSubtypeFilterEnabled: Int,
        isSetFilterEnabled: Int
    ) {
        setPref(SEARCH_SUBTYPE_KEY, selectedSubtype)
        setPref(SEARCH_SET_KEY, selectedSet)
        setPref(SEARCH_ORDER_KEY, selectedOrder)
        setPref(SEARCH_SUBTYPE_ACTIVE_KEY, isSubtypeFilterEnabled)
        setPref(SEARCH_SET_ACTIVE_KEY, isSetFilterEnabled)
    }
}