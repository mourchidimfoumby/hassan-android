package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVersePreferences(
    val displayTranslation: Boolean,
    val translationLanguage: Language?,
    val reciter: Reciter?,
    val displayMode: DisplayMode
) {
    enum class DisplayMode {
        LIST, PAGE
    }
}