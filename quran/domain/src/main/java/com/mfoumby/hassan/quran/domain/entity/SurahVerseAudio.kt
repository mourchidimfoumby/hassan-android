package com.mfoumby.hassan.quran.domain.entity

data class SurahVerseAudio(
    val surah: Surah,
    val verseNumber: Int,
    val audioUri: String
) {
    val id = surah.number * 1000 + verseNumber

    companion object {
        fun getSurahNumberFromId(id: Int) = id / 1000

        fun getVerseNumberFromId(id: Int) = id % 1000
    }
}