package pt.mf.mybinder.domain.usecase

import okio.IOException
import pt.mf.mybinder.utils.Result
import retrofit2.Response

/**
 * Created by Martim Ferreira on 09/02/2025
 */
abstract class BaseUseCase {
    protected suspend fun <T> performHttpRequest(
        request: suspend () -> Response<T>,
    ): Result<T> {
        return try {
            val resp = request.invoke()

            if (!resp.isSuccessful)
                Result.Error(Exception(formatErrorMessage(resp)))

            if (resp.body() == null)
                Result.Error(Exception("Null body."))
            else
                Result.Success(resp.body()!!)

        } catch (e: IOException) {
            Result.Error(e)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    private fun <T> formatErrorMessage(response: Response<T>): String {
        return "${response.code()} ${response.errorBody()}"
    }
}