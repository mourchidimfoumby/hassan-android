package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.common.domain.usecase.GetCurrentLanguageUseCase
import com.mfoumby.hassan.quran.data.repository.SurahRepositoryImpl.Companion.MAX_SURAH_COUNT
import com.mfoumby.hassan.quran.domain.repository.SurahRepository
import org.koin.java.KoinJavaComponent.inject

class InitSurahsWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val surahRepository: SurahRepository by inject(SurahRepository::class.java)
    private val getCurrentLanguageUseCase: GetCurrentLanguageUseCase by inject(GetCurrentLanguageUseCase::class.java)

    override suspend fun doWork(): Result {
        return try {
            if (surahRepository.getSurahCount() < MAX_SURAH_COUNT) {
                surahRepository.downloadSurahs(getCurrentLanguageUseCase.execute())
            }
            Result.success()
        } catch (e: Exception) {
            e("Error downloading surahs", e)
            Result.failure()
        }
    }
}