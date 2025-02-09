package pt.mf.mybinder.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.mf.mybinder.data.repository.remote.SetRepositoryImpl
import pt.mf.mybinder.domain.usecase.remote.SetUseCase
import pt.mf.mybinder.utils.Logger

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SetUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "SetUpdateWorker"
    }

    override suspend fun doWork(): Result {
        val result = withContext(Dispatchers.IO) {
            Logger.debug(TAG, "Running periodic $TAG routine.")
            SetUseCase(SetRepositoryImpl()).fetchSets()
        }

        return when (result) {
            is pt.mf.mybinder.utils.Result.Success -> {
                Logger.debug(TAG, "Updated sets successfully.")
                Result.success()
            }

            is pt.mf.mybinder.utils.Result.Error -> {
                Logger.debug(TAG, "Failed to update sets.")
                Result.failure()
            }
        }
    }
}