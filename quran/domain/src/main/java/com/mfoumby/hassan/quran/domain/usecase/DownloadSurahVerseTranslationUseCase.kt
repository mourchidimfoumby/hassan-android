package com.mfoumby.hassan.quran.domain.usecase

import com.mfoumby.hassan.common.domain.entity.Progress
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.entity.Constants.TOTAL_QURAN_VERSES
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

class DownloadSurahVerseTranslationUseCase(
    private val surahVerseTranslationRepository: SurahVerseTranslationRepository,
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository
) {
    private val _downloadingProgress = MutableStateFlow<TranslationLanguage?>(null)
    val downloadingProgress: Flow<TranslationLanguage> = _downloadingProgress.filterNotNull()

    suspend fun execute(translationLanguage: TranslationLanguage) {
        if (translationLanguage.state !is TranslationLanguageState.NotDownloaded) return
        var verseCount = 0

        surahVerseTranslationRepository.downloadSurahVerseTranslations(translationLanguage.language)
            .catch {
                _downloadingProgress.update {
                    translationLanguage.copy(state = TranslationLanguageState.NotDownloaded)
                }
            }
            .collect {
                verseCount += it.size
                _downloadingProgress.update {
                    translationLanguage.copy(
                        state = TranslationLanguageState.Downloading(Progress(verseCount, TOTAL_QURAN_VERSES).progress)
                    )
                }
            }

        surahVerseTranslationLanguageRepository.updateTranslationLanguage(translationLanguage.copy(state = TranslationLanguageState.Downloaded))
        _downloadingProgress.update {
            translationLanguage.copy(state = TranslationLanguageState.Downloaded)
        }
    }
}