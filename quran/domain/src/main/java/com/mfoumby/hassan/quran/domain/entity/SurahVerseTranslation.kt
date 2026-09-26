package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVerseTranslation(
    val verseNumber: Int,
    val surahNumber: Int,
    val juzNumber: Int,
    val hizbNumber: Int,
    val translation: String,
    val language: Language
)
