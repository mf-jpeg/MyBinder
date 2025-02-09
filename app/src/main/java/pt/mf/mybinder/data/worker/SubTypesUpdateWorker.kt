package pt.mf.mybinder.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.mf.mybinder.data.model.local.SubType
import pt.mf.mybinder.data.repository.SubTypesRepositoryImpl
import pt.mf.mybinder.domain.usecase.SubTypesUseCase
import pt.mf.mybinder.utils.Logger

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class SubTypesUpdateWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "SubTypesUpdateWorker"
    }

    override suspend fun doWork(): Result {
        val useCase = SubTypesUseCase(SubTypesRepositoryImpl())

        val result = withContext(Dispatchers.IO) {
            Logger.debug(TAG, "Running periodic $TAG routine.")
            useCase.remoteFetchSubTypes()
        }

        return when (result) {
            is pt.mf.mybinder.utils.Result.Success -> {
                val subtypes = mutableListOf<SubType>()

                result.data.data.forEach {
                    subtypes.add(useCase.convertRemoteToLocalSubType(it))
                }

                useCase.insertSubTypes(subtypes)
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