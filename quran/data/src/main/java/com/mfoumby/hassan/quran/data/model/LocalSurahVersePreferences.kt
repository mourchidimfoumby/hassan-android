package com.mfoumby.hassan.quran.data.model

data class LocalSurahVersePreferences(
    val displayMode: String,
    val displayTajweed: Boolean,
    val translationLanguage: String?,
    val displayTranslation: Boolean,
    val reciter: String?,
    val audioAutomaticScrolling: Boolean,
    val surahBookmark: String?,
    val juzBookmark: String?,
    val hizbBookmark: String?
)
