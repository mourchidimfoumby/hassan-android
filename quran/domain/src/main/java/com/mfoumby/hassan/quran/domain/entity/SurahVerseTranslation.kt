package com.mfoumby.hassan.quran.domain.entity

import com.mfoumby.hassan.common.domain.entity.Language

data class SurahVerseTranslation(
    val number: Int,
    val surahNumber: Int,
    val translation: String,
    val language: Language
)
