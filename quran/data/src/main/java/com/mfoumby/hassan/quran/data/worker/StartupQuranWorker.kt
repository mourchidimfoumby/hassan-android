package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class StartupQuranWorker(context: Context) {
    private val workerManager = WorkManager.getInstance(context)

    fun run() {
        workerManager.enqueue(sendUnsentMessageWorkRequest())
    }

    private fun sendUnsentMessageWorkRequest(): OneTimeWorkRequest {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        return OneTimeWorkRequestBuilder<InitSurahsWorker>()
            .setConstraints(constraints)
            .build()
    }

}