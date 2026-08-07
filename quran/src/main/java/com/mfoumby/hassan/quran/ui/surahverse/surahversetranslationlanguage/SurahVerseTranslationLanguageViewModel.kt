package com.mfoumby.hassan.quran.ui.surahverse.surahversetranslationlanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfoumby.hassan.common.SingleUiEvent
import com.mfoumby.hassan.common.domain.entity.Language
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage
import com.mfoumby.hassan.common.domain.entity.TranslationLanguage.TranslationLanguageState
import com.mfoumby.hassan.quran.domain.entity.SurahVersePreferences
import com.mfoumby.hassan.quran.domain.repository.SurahVersePreferencesRepository
import com.mfoumby.hassan.quran.domain.repository.SurahVerseTranslationLanguageRepository
import com.mfoumby.hassan.quran.domain.usecase.DeleteTranslationLanguageUseCase
import com.mfoumby.hassan.quran.domain.usecase.DownloadSurahVerseTranslationUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SurahVerseTranslationLanguageViewModel(
    private val surahVersePreferencesRepository: SurahVersePreferencesRepository,
    private val surahVerseTranslationLanguageRepository: SurahVerseTranslationLanguageRepository,
    private val downloadSurahVerseTranslationUseCase: DownloadSurahVerseTranslationUseCase,
    private val deleteTranslationLanguageUseCase: DeleteTranslationLanguageUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SurahVerseTranslationLanguageUiState())
    val uiState: StateFlow<SurahVerseTranslationLanguageUiState> = _uiState.asStateFlow()
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event = _event.asSharedFlow()
    private var downloadingJobs = linkedMapOf<Language, Job>()

    init {
        initUiState()
    }

    fun onTranslationLanguageSelect(translationLanguage: TranslationLanguage) {
        val surahVersePreferences = uiState.value.preferences ?: return
        viewModelScope.launch {
            when (translationLanguage.state) {
               TranslationLanguageState.Downloaded -> {
                   if (surahVersePreferences.translationLanguage == translationLanguage.language) {
                       surahVersePreferencesRepository.setSurahVersePreferences(
                           surahVersePreferences.copy(translationLanguage = null)
                       )
                   } else {
                       surahVersePreferencesRepository.setSurahVersePreferences(
                           surahVersePreferences.copy(translationLanguage = translationLanguage.language)
                       )
                   }
               }

               TranslationLanguageState.NotDownloaded -> {
                   val job = launch {
                       try {
                           downloadSurahVerseTranslationUseCase.execute(translationLanguage).collect { translationLanguage ->
                               _uiState.update { state ->
                                   state.translationLanguages?.map {
                                       if (it.language == translationLanguage.language) {
                                           translationLanguage
                                       } else it
                                   }?.let {
                                       state.copy(translationLanguages = it)
                                   } ?: state
                               }
                           }

                           if (downloadingJobs.keys.lastOrNull() == translationLanguage.language) {
                               surahVersePreferencesRepository.setSurahVersePreferences(
                                   surahVersePreferences.copy(translationLanguage = translationLanguage.language)
                               )
                           }
                       } catch (_: Exception) {
                           _event.emit(SurahVerseTranslationUiEvent.SurahVerseTranslationDownloadError(translationLanguage))
                       } finally {
                           cancelDownloadingJob(translationLanguage)
                       }
                   }
                   downloadingJobs[translationLanguage.language] = job
               }

               else -> {}
           }
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

    private fun initUiState() {
        val preferencesFlow = surahVersePreferencesRepository.getSurahVersePreferencesFlow()
        val translationLanguagesFlow = surahVerseTranslationLanguageRepository.getTranslationLanguagesFlow()

        combine(
            preferencesFlow,
            translationLanguagesFlow
        ) { preferences, translationLanguages ->
            _uiState.update {
                it.copy(
                    preferences = preferences,
                    translationLanguages = translationLanguages,
                    isLoading = false
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun cancelDownloadingJob(translationLanguage: TranslationLanguage) {
        downloadingJobs[translationLanguage.language]?.cancel()
        downloadingJobs.remove(translationLanguage.language)
    }

    data class SurahVerseTranslationLanguageUiState(
        val preferences: SurahVersePreferences? = null,
        val translationLanguages: List<TranslationLanguage>? = null,
        val isLoading: Boolean = true
    )

    sealed interface SurahVerseTranslationUiEvent: SingleUiEvent {
        data class SurahVerseTranslationDownloadError(val translationLanguage: TranslationLanguage): SurahVerseTranslationUiEvent
    }
}