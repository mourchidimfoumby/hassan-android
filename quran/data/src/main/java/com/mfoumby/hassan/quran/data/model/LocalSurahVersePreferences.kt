package com.mfoumby.hassan.quran.data.model

data class LocalSurahVersePreferences(
    val displayMode: String,
    val displayTajweed: Boolean,
    val displayTranslation: Boolean,
    val arabicTextFont: String,
    val arabicTextFontSize: Int,
    val translationLanguage: String?,
    val reciter: String?,
    val surahBookmark: String?,
    val juzBookmark: String?,
    val hizbBookmark: String?
)
