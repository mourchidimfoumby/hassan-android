package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVersePreferences(
    val displayTranslation: Boolean,
    val translationLanguage: Language?,
    val reciter: Reciter?,
    val displayMode: DisplayMode,
    val surahBookmark: SurahVerse?,
    val juzBookmark: SurahVerse?,
    val hizbBookmark: SurahVerse?
) {
    enum class DisplayMode {
        LIST, PAGE
    }
}