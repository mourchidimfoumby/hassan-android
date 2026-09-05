package com.mfoumby.hassan.quran.domain.entity

data class Verse(
    val verseNumber: Int,
    val surahNumber: Int,
    val text: String,
    val transliteration: String,
    val page: Int,
    val juzNumber: Int,
    val hizbNumber: Int
)