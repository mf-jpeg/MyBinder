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
        const val DEFAULT_WORKER_FREQ = 7L
    }

    fun scheduleWorkers() {
        Logger.debug(TAG, "Initilizing background workers.")

        val setPeriodicRequest =
            generateWorkRequest<SetUpdateWorker>(DEFAULT_WORKER_FREQ, TimeUnit.DAYS)

        val subTypesPeriodicRequest =
            generateWorkRequest<SubtypeUpdateWorker>(DEFAULT_WORKER_FREQ, TimeUnit.DAYS)

        Logger.debug(TAG, "Enqueueing SetUpdateWorker.")
        enqueueWorkRequest(SetUpdateWorker.TAG, setPeriodicRequest)

        Logger.debug(TAG, "Enqueueing SubTypesUpdateWorker.")
        enqueueWorkRequest(SubtypeUpdateWorker.TAG, subTypesPeriodicRequest)

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