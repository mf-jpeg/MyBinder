package pt.mf.mybinder.utils

import android.annotation.SuppressLint
import android.content.Context
import pt.mf.mybinder.utils.Utils.empty

/**
 * Created by Martim Ferreira on 09/02/2025
 */
object PreferencesManager {
    const val TAG = "Preferences"
    const val PREF_KEY = "preferences"

    const val SETS_READY_KEY = "sets_ready"
    const val SUBTYPES_READY_KEY = "subtypes_ready"

    const val SEARCH_SUBTYPE_KEY = "search_subtype"
    const val SEARCH_SUBTYPE_ACTIVE_KEY = "search_subtype_active"

    const val SEARCH_SET_KEY = "search_set"
    const val SEARCH_SET_ACTIVE_KEY = "search_set_active"

    const val SEARCH_ORDER_KEY = "search_order"

    @SuppressLint("ApplySharedPref")
    fun <T> setPref(key: String, value: T) {
        val sp =
            MyBinder.ctx.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        val editor = sp.edit()

        when (value) {
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            else -> throw UnsupportedOperationException("Unsupported type")
        }

        Logger.debug(TAG, "Setting key \"$key\" to value \"$value\".")
        editor.commit()
    }

    inline fun <reified T> getPref(key: String): T {
        val sp =
            MyBinder.ctx.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        return when (T::class) {
            String::class -> sp.getString(key, String.empty()) as T
            Boolean::class -> sp.getBoolean(key, false) as T
            Int::class -> sp.getInt(key, 0) as T
            Long::class -> sp.getLong(key, 0L) as T
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }
}