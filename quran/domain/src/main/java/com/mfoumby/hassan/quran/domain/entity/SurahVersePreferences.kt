package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVersePreferences(
    val displayMode: DisplayMode,
    val displayTajweed: Boolean,
    val translationLanguage: Language?,
    val displayTransliteration: Boolean,
    val displayTranslation: Boolean,
    val reciter: Reciter?,
    val audioAutomaticScrolling: Boolean,
    val surahBookmark: SurahVerse?,
    val juzBookmark: SurahVerse?,
    val hizbBookmark: SurahVerse?
) {
    enum class DisplayMode {
        LIST, PAGE
    }
}