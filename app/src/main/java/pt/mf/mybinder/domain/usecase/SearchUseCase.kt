package pt.mf.mybinder.domain.usecase

import pt.mf.mybinder.utils.PreferencesManager.SEARCH_ORDER_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SET_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_ACTIVE_KEY
import pt.mf.mybinder.utils.PreferencesManager.SEARCH_SUBTYPE_KEY
import pt.mf.mybinder.utils.PreferencesManager.setPref

/**
 * Created by Martim Ferreira on 10/02/2025
 */
class SearchUseCase {
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