package com.mfoumby.hassan.quran.domain.entity

sealed interface QuranSearchResult {
    val type: QuranSearchResultType

    data class SurahResult(val surahs: List<Surah>): QuranSearchResult {
        override val type = QuranSearchResultType.SURAH_RESULT
    }
}

enum class QuranSearchResultType {
    SURAH_RESULT
}