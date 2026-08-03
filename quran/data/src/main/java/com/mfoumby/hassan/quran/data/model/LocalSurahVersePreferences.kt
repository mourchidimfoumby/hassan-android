package com.mfoumby.hassan.quran.data.model

data class LocalSurahVersePreferences(
    val displayTranslation: Boolean,
    val translationLanguage: String?,
    val reciter: String?,
    val displayMode: String,
    val surahBookmark: String?,
    val juzBookmark: String?,
    val hizbBookmark: String?
)
