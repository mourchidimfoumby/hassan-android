package com.mfoumby.hassan.quran.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.mfoumby.hassan.common.data.e
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import org.koin.java.KoinJavaComponent.inject

class InitSurahVerseTranslationLanguagesWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository by inject(SurahVerseTranslationLanguageRepository::class.java)

    override suspend fun doWork(): Result {
        return try {
            val remoteTranslationLanguages = surahVerseTranslationLanguageRepository.fetchTranslationLanguages().sorted()
            val localTranslationLanguages = surahVerseTranslationLanguageRepository.getTranslationLanguages()
                .map { it.language }
                .sortedBy { it.code }

            when {
                localTranslationLanguages.isEmpty() -> {
                    val translationLanguages = remoteTranslationLanguages.map { language ->
                        TranslationLanguage(
                            language = language,
                            state = TranslationLanguageState.NotDownloaded
                        )
                    }
                    surahVerseTranslationLanguageRepository.setTranslationLanguages(translationLanguages)
                }

                remoteTranslationLanguages != localTranslationLanguages -> {
                    val translationLanguages = remoteTranslationLanguages.map { language ->
                        val state = if (language in localTranslationLanguages) TranslationLanguageState.Downloaded else TranslationLanguageState.NotDownloaded
                        TranslationLanguage(
                            language = language,
                            state = state
                        )
                    }
                    surahVerseTranslationLanguageRepository.setTranslationLanguages(translationLanguages)
                }

                else -> Unit
            }

            Result.success()
        } catch (e: Exception) {
            e("Error init surah verse translation languages", e)
            Result.failure()
        }
    }
}