package com.mfoumby.hassan.quran.domain

data class Surah(
    val number: Int,
    val name: String,
    val transliteration: String,
    val type: String,
    val totalVerses: Int,
    val translation: String
)