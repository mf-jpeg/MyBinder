package pt.mf.mybinder.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.mf.mybinder.data.model.local.Subtype
import pt.mf.mybinder.data.repository.SubtypeRepositoryImpl
import pt.mf.mybinder.domain.usecase.SubtypeUseCase
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.PreferencesManager
import pt.mf.mybinder.utils.PreferencesManager.setPref

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubtypeUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "SubTypesUpdateWorker"
    }

    override suspend fun doWork(): Result {
        val useCase = SubtypeUseCase(SubtypeRepositoryImpl())

        val result = withContext(Dispatchers.IO) {
            Logger.debug(TAG, "Running periodic $TAG routine.")
            useCase.fetchRemoteSubtypes()
        }

        return when (result) {
            is pt.mf.mybinder.utils.Result.Success -> {
                val subtypes = mutableListOf<Subtype>()

                result.data.data.forEach {
                    subtypes.add(useCase.convertRemoteToLocalSubType(it))
                }

                useCase.insertSubTypes(subtypes)

                if (subtypes.isNotEmpty())
                    setPref(PreferencesManager.SUBTYPES_READY_KEY, true)

                Logger.debug(TAG, "Updated subtypes successfully.")
                Result.success()
            }

            is pt.mf.mybinder.utils.Result.Error -> {
                Logger.debug(TAG, "Failed to update subtypes.")
                Result.failure()
            }
        }
    }
}