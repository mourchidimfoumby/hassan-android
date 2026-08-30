package com.mfoumby.hassan.quran.domain.entity

object Constants {
    const val TOTAL_QURAN_SURAH = 114
    const val TOTAL_QURAN_VERSES = 6236
    const val TOTAL_QURAN_PAGES = 604
    const val TOTAL_QURAN_JUZ = 30
    const val TOTAL_QURAN_HIZB = 60
    val DEFAULT_SURAH_VERSE_PREFERENCES = SurahVersePreferences(
        displayMode = SurahVersePreferences.DisplayMode.LIST,
        displayTajweed = false,
        displayTranslation = true,
        arabicTextFont = ArabicTextFont.UTHMANIC,
        arabicTextFontSize = 34,
        translationLanguage = null,
        reciter = null,
        surahBookmark = null,
        juzBookmark = null,
        hizbBookmark = null
    )
}