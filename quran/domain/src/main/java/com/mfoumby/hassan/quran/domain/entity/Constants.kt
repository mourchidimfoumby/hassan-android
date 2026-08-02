package com.mfoumby.hassan.quran.domain.entity

object Constants {
    const val TOTAL_QURAN_SURAH = 114
    const val TOTAL_QURAN_VERSES = 6236
    const val TOTAL_QURAN_PAGES = 604
    const val TOTAL_QURAN_JUZ = 30
    val DEFAULT_PREFERENCES = SurahVersePreferences(
        displayTranslation = true,
        translationLanguage = null,
        reciter = null,
        displayMode = SurahVersePreferences.DisplayMode.LIST
    )
}