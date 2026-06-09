package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.common.domain.usecase.LanguageUseCase
import com.mfoumby.hassan.quran.data.repository.SurahVerseRepositoryImpl.Companion.MAX_SURAH_VERSES
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import org.koin.java.KoinJavaComponent.inject

class InitSurahVerseTranslationsWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository by inject(SurahVerseTranslationRepository::class.java)
    private val languageUseCase: LanguageUseCase by inject(LanguageUseCase::class.java)

    override suspend fun doWork(): Result {
        val language = languageUseCase.getCurrentLanguage()
        return try {
            if (surahVerseTranslationRepository.getSurahVerseTranslationCount(language) < MAX_SURAH_VERSES) {
                surahVerseTranslationRepository.downloadSurahVerseTranslations(language)
            }
            Result.success()
        } catch (e: Exception) {
            e("Error downloading surah verse translations", e)
            Result.failure()
        }
    }
}