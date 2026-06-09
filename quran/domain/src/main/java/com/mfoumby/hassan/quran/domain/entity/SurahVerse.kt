package com.mfoumby.hassan.quran.domain.entity

data class SurahVerse(
    val number: Int,
    val surahNumber: Int,
    val text: String,
    val translation: String? = null
)