package pt.mf.mybinder.utils

import android.util.Log

/**
 * Created by Martim Ferreira on 07/02/2025
 */
object Logger {
    fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    fun error(tag: String, message: String) {
        Log.e(tag, message)
    }

    fun warn(tag: String, message: String) {
        Log.w(tag, message)
    }
}