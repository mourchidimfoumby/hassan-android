package com.mfoumby.hassan.quran.domain.usecase

import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.entity.Constants.TOTAL_QURAN_VERSES
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class DownloadSurahVerseTranslationUseCase(
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository
) {
    fun execute(translationLanguage: TranslationLanguage): Flow<TranslationLanguage> {
        if (translationLanguage.state !is TranslationLanguageState.NotDownloaded) return emptyFlow()
        var verseCount = 0
        return surahVerseTranslationRepository
            .downloadSurahVerseTranslations(translationLanguage.language)
            .map { surahVerseTranslation ->
                verseCount += surahVerseTranslation.size
                translationLanguage.copy(
                    state = TranslationLanguageState.Downloading(
                        Progress(verseCount, TOTAL_QURAN_VERSES).progress
                    )
                )
            }
            .onEach(surahVerseTranslationLanguageRepository::updateTranslationLanguage)
            .onCompletion { cause ->
                if (cause == null) {
                    val downloaded = translationLanguage.copy(state = TranslationLanguageState.Downloaded)
                    surahVerseTranslationLanguageRepository.updateTranslationLanguage(downloaded)
                }
            }
            .catch { e ->
                val notDownloaded = translationLanguage.copy(state = TranslationLanguageState.NotDownloaded)
                surahVerseTranslationLanguageRepository.updateTranslationLanguage(notDownloaded)
                emit(notDownloaded)
                throw e
            }
    }
}