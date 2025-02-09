package pt.mf.mybinder.data.worker

import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.MyBinder.Companion.ctx
import java.util.concurrent.TimeUnit

/**
 * Created by Martim Ferreira on 09/02/2025
 */
class WorkManager {
    companion object {
        const val TAG = "WorkManager"
        const val DEFAULT_WORKER_FREQUENCY = 7
    }

    fun scheduleWorkers() {
        Logger.debug(TAG, "Initilizing background workers.")

        val setPeriodicRequest =
            generateWorkRequest<SetUpdateWorker>(15, TimeUnit.MINUTES)

        val subTypesPeriodicRequest =
            generateWorkRequest<SubTypesUpdateWorker>(15, TimeUnit.MINUTES)

        Logger.debug(TAG, "Enqueueing SetUpdateWorker.")
        enqueueWorkRequest(SetUpdateWorker.TAG, setPeriodicRequest)

        Logger.debug(TAG, "Enqueueing SubTypesUpdateWorker.")
        enqueueWorkRequest(SubTypesUpdateWorker.TAG, subTypesPeriodicRequest)

        Logger.debug(TAG, "Finished scheduling workers.")
    }

    private inline fun <reified T : CoroutineWorker> generateWorkRequest(
        interval: Long,
        timeUnit: TimeUnit
    ): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<T>(interval, timeUnit)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            ).build()
    }

    private fun enqueueWorkRequest(tag: String, workRequest: PeriodicWorkRequest) {
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            tag,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}