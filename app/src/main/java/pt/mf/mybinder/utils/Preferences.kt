package pt.mf.mybinder.utils

import android.annotation.SuppressLint
import android.content.Context

/**
 * Created by Martim Ferreira on 09/02/2025
 */
object Preferences {
    const val TAG = "Preferences"
    const val PREF_KEY = "preferences"

    const val SETS_READY_KEY = "sets_ready"
    const val SUBTYPES_READY_KEY = "subtypes_ready"

    @SuppressLint("ApplySharedPref")
    fun <T> setPref(key: String, value: T) {
        val sp =
            MyBinder.ctx.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

        val editor = sp.edit()

        when (value) {
            is String -> editor.putString(key, value)
            is Boolean -> editor.putBoolean(key, value)
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
            String::class -> sp.getString(key, "") as T
            Boolean::class -> sp.getBoolean(key, false) as T
            Long::class -> sp.getLong(key, 0L) as T
            else -> throw UnsupportedOperationException("Unsupported type")
        }
    }
}