package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVersePreferences(
    val displayMode: DisplayMode,
    val displayTajweed: Boolean,
    val displayTranslation: Boolean,
    val arabicTextFont: ArabicTextFont,
    val arabicTextFontSize: Int,
    val translationLanguage: Language?,
    val reciter: Reciter?,
    val surahBookmark: SurahVerse?,
    val juzBookmark: SurahVerse?,
    val hizbBookmark: SurahVerse?
) {
    enum class DisplayMode {
        LIST, PAGE
    }
}