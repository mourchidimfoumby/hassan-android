package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.quran.domain.repository.ReciterRepository
import org.koin.java.KoinJavaComponent.inject

class InitRecitersWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val reciterRepository: ReciterRepository by inject(ReciterRepository::class.java)

        override suspend fun doWork(): Result {
            return try {
                val reciters = reciterRepository.getReciters()
                if (reciters.size < reciterRepository.fetchReciterCount()) {
                    reciterRepository.downloadReciters()
                }
                Result.success()
            } catch (e: Exception) {
                e("Error init reciters", e)
                Result.failure()
            }
        }
}