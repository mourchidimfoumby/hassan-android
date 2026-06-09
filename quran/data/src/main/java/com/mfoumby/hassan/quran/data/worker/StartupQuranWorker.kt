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
        workerManager.enqueue(buildWorkRequest<InitSurahVerseTranslationsWorker>())
    }

    private inline fun <reified T : ListenableWorker>buildWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<T>()
            .setConstraints(constraints)
            .build()
    }
}