package pt.mf.mybinder.utils

import java.lang.Exception

/**
 * Created by Martim Ferreira on 08/02/2025
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}