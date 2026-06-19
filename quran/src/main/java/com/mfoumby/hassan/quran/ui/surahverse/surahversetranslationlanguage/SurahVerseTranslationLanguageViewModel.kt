package com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.usecase.DeleteTranslationLanguageUseCase
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurahVerseTranslationLanguageViewModel(
    private val surahPreferencesRepository: SurahVersePreferencesRepository,
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository,
    private val downloadSurahVerseTranslationUseCase: DownloadSurahVerseTranslationUseCase,
    private val deleteTranslationLanguageUseCase: DeleteTranslationLanguageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseTranslationLanguageUiState())
    val uiState: StateFlow<SurahVerseTranslationLanguageUiState> = _uiState.asStateFlow()
    private var downloadingJobs: MutableMap<Language, Job> = mutableMapOf()

    init {
        listenSurahVersePreferences()
        listenTranslationLanguages()
        listenSurahVerseTranslationDownloading()
    }

    fun onTranslationLanguageSelect(translationLanguage: TranslationLanguage) {
        val surahVersePreferences = uiState.value.surahVersePreferences ?: return
        viewModelScope.launch {
           when (translationLanguage.state) {
               TranslationLanguageState.Downloaded -> {
                   if (surahVersePreferences.translationLanguage == translationLanguage.language) {
                       surahPreferencesRepository.setSurahVersePreferences(
                           surahVersePreferences.copy(translationLanguage = null)
                       )
                   } else {
                       surahPreferencesRepository.setSurahVersePreferences(
                           surahVersePreferences.copy(translationLanguage = translationLanguage.language)
                       )
                   }
               }

               TranslationLanguageState.NotDownloaded -> downloadSurahVerseTranslationUseCase.execute(translationLanguage)

               else -> {}
           }
        }.also {
            downloadingJobs[translationLanguage.language] = it
        }
    }

    fun onDeleteTranslationLanguage(translationLanguage: TranslationLanguage) {
        viewModelScope.launch {
            deleteTranslationLanguageUseCase.execute(translationLanguage)
        }
    }

    fun onCancelTranslationLanguageDownload(translationLanguage: TranslationLanguage) {
        cancelDownloadingJob(translationLanguage)
    }

    private fun listenSurahVersePreferences() {
        viewModelScope.launch {
            surahPreferencesRepository.getSurahVersePreferencesFlow().collect { surahPreferences ->
                _uiState.update {
                    it.copy(surahVersePreferences = surahPreferences)
                }
                refreshInitializingState()
            }
        }
    }

    private fun listenTranslationLanguages() {
        viewModelScope.launch {
            surahVerseTranslationLanguageRepository.getTranslationLanguagesFlow().collect { translationLanguages ->
                _uiState.update {
                    it.copy(translationLanguages = translationLanguages)
                }
                refreshInitializingState()
            }
        }
    }

    private fun refreshInitializingState() {
        if (!uiState.value.initializing) return
        _uiState.update {
            it.copy(initializing = !it.initialized)
        }
    }

    private fun listenSurahVerseTranslationDownloading() {
        viewModelScope.launch {
            downloadSurahVerseTranslationUseCase.downloadingProgress.collect { translationLanguage ->
                _uiState.update { state ->
                    val translationLanguages = state.translationLanguages?.map {
                        if (it.language == translationLanguage.language) translationLanguage else it
                    } ?: return@update state
                    state.copy(translationLanguages = translationLanguages)
                }

                if (translationLanguage.state is TranslationLanguageState.Downloaded) {
                    cancelDownloadingJob(translationLanguage)
                }
            }
        }
    }

    private fun cancelDownloadingJob(translationLanguage: TranslationLanguage) {
        downloadingJobs[translationLanguage.language]?.cancel()
        downloadingJobs.remove(translationLanguage.language)
    }

    data class SurahVerseTranslationLanguageUiState(
        val surahVersePreferences: SurahVersePreferences? = null,
        val translationLanguages: List<TranslationLanguage>? = null,
        val initializing: Boolean = true
    ) {
        val initialized: Boolean
            get() = surahVersePreferences != null &&
                    translationLanguages != null
    }
}