package com.mfoumby.hassan.quran.domain.entity

data class Verse(
    val verseNumber: Int,
    val surahNumber: Int,
    val text: String,
    val page: Int,
    val juz: Int,
    val hizb: Int
)