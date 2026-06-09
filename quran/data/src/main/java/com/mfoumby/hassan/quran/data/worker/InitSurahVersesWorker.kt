package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.quran.data.repository.SurahVerseRepositoryImpl.Companion.MAX_SURAH_VERSES
import com.mfoumby.hassan.quran.domain.repository.SurahVerseRepository
import org.koin.java.KoinJavaComponent.inject

class InitSurahVersesWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val surahVerseRepository: SurahVerseRepository by inject(SurahVerseRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            if (surahVerseRepository.getSurahVersesCount() < MAX_SURAH_VERSES) {
                surahVerseRepository.downloadSurahVerses()
            }
            Result.success()
        } catch (e: Exception) {
            e("Error downloading surah verses", e)
            Result.failure()
        }
    }
}