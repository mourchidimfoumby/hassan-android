package com.mfoumby.hassan.quran

typealias SurahNumber = Int
typealias JuzNumber = Int
typealias HizbNumber = Int
typealias VerseNumber = Int

sealed class QuranMode(
    open val surahNumber: SurahNumber,
    open val verseNumber: VerseNumber?
) {
    data class SurahMode(
        override val surahNumber: SurahNumber,
        override val verseNumber: VerseNumber?
    ): QuranMode(surahNumber, verseNumber)

    data class JuzMode(
        val juzNumber: JuzNumber,
        override val surahNumber: SurahNumber,
        override val verseNumber: VerseNumber?
    ): QuranMode(surahNumber, verseNumber)

    data class HizbMode(
        val hizbNumber: HizbNumber,
        override val surahNumber: SurahNumber,
        override val verseNumber: VerseNumber?
    ): QuranMode(surahNumber, verseNumber)
}