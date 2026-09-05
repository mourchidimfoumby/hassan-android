package com.mfoumby.hassan.quran.data.model

data class LocalSurahVersePreferences(
    val displayMode: String,
    val translationLanguage: String?,
    val displayTransliteration: Boolean,
    val displayTranslation: Boolean,
    val displayTajweed: Boolean,
    val reciter: String?,
    val audioAutomaticScrolling: Boolean,
    val surahBookmark: String?,
    val juzBookmark: String?,
    val hizbBookmark: String?
)
