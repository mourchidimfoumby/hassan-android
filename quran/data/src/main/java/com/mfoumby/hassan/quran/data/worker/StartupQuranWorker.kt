package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class StartupQuranWorker(context: Context) {
    private val workerManager = WorkManager.getInstance(context)

    fun run() {
        workerManager.enqueue(buildWorkRequest<InitSurahsWorker>())
        workerManager.enqueue(buildWorkRequest<InitSurahVersesWorker>())
        workerManager.enqueue(buildWorkRequest<InitSurahVerseTranslationLanguagesWorker>())
        workerManager.enqueue(buildWorkRequest<InitSurahVersePreferencesWorker>(
            Parameters(networkType = NetworkType.NOT_REQUIRED)
        ))
        workerManager.enqueue(buildWorkRequest<InitRecitersWorker>())
    }

    private inline fun <reified T : ListenableWorker>buildWorkRequest(parameters: Parameters = Parameters()): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(parameters.networkType)
            .build()

        return OneTimeWorkRequestBuilder<T>()
            .setConstraints(constraints)
            .build()
    }

    private data class Parameters(
        val networkType: NetworkType = NetworkType.CONNECTED
    )
}