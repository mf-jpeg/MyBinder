package pt.mf.mybinder.domain.usecase

import pt.mf.mybinder.data.model.remote.CardResponse
import pt.mf.mybinder.data.model.remote.SearchResponse
import pt.mf.mybinder.domain.repository.SearchRepository
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_ORDER_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_KEY
import pt.mf.mybinder.utils.PreferencesManager.setPref
import pt.mf.mybinder.utils.Result
import pt.mf.mybinder.utils.Utils

/**
 * Created by Martim Ferreira on 10/02/2025
 */
class SearchUseCase(
    private val repository: SearchRepository
) : BaseUseCase() {

    private companion object {
        const val TAG = "SearchUseCase"
        const val DEFAULT_PAGE_SIZE = 10
        const val DEFAULT_PAGE_NUMBER = 1
    }

    suspend fun fetchCards(
        name: String,
        subtype: String,
        setId: String,
        isSubtypeFilterEnabled: Boolean,
        isSetFilterEnabled: Boolean,
        pageSize: Int = DEFAULT_PAGE_SIZE,
        pageNumber: Int = DEFAULT_PAGE_NUMBER,
        selectedOrder: Int
    ): Result<SearchResponse> {
        return performHttpRequest {
            repository.fetchCards(
                formatQuery(name, subtype, setId, isSubtypeFilterEnabled, isSetFilterEnabled),
                pageSize,
                pageNumber,
                formatOrderBy(selectedOrder)
            )
        }
    }

    suspend fun fetchCard(id: String): Result<CardResponse> {
        return performHttpRequest {
            repository.fetchCard(id)
        }
    }

    private fun formatQuery(
        name: String,
        subtype: String,
        setId: String,
        isSubtypeFilterEnabled: Boolean,
        isSetFilterEnabled: Boolean,
    ): String {
        val sb = StringBuilder()

        if (name.isNotEmpty())
            sb.append("name:${Utils.addEnclosingQuotes(name)} ")

        if (isSubtypeFilterEnabled) {
            if (subtype.isNotEmpty())
                sb.append("subtypes:${Utils.addEnclosingQuotes(subtype)} ")
        }

        if (isSetFilterEnabled) {
            if (setId.isNotEmpty())
                sb.append("set.id:$setId ")
        }

        Logger.debug(TAG, "Formatted query: \"${sb.trim()}\".")
        return sb.toString().trim()
    }

    // TODO: Refactor to be extensible.
    private fun formatOrderBy(selectedOrder: Int): String {
        return when (selectedOrder) {
            0 -> "name"
            1 -> "-set.releaseDate"
            else -> "name"
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