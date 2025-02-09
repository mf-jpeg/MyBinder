package pt.mf.mybinder.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.mf.mybinder.data.model.local.Set
import pt.mf.mybinder.data.repository.SetRepositoryImpl
import pt.mf.mybinder.domain.usecase.SetUseCase
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Preferences

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
        val useCase = SetUseCase(SetRepositoryImpl())

        val result = withContext(Dispatchers.IO) {
            Logger.debug(TAG, "Running periodic $TAG routine.")
            useCase.fetchRemoteSets()
        }

        return when (result) {
            is pt.mf.mybinder.utils.Result.Success -> {
                val sets = mutableListOf<Set>()

                result.data.data.forEach {
                    sets.add(useCase.convertRemoteToLocalSet(it))
                }

                useCase.insertSets(sets)

                if (sets.isNotEmpty())
                    Preferences.setPref(Preferences.SETS_READY_KEY, true)

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