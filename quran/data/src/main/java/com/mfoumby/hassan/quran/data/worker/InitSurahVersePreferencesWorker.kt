package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.quran.domain.entity.Constants.DEFAULT_PREFERENCES
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import org.koin.java.KoinJavaComponent.inject

class InitSurahVersePreferencesWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository by inject(SurahVersePreferencesRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            if (surahVersePreferencesRepository.getSurahVersePreferences() == null) {
                surahVersePreferencesRepository.setSurahVersePreferences(DEFAULT_PREFERENCES)
            }
            Result.success()
        } catch (e: Exception) {
            e("Error setting surah verse preferences", e)
            Result.failure()
        }
    }
}