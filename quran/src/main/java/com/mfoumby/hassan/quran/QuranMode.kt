package com.mfoumby.hassan.quran

import com.mfoumby.hassan.quran.domain.HizbNumber
import com.mfoumby.hassan.quran.domain.JuzNumber
import com.mfoumby.hassan.quran.domain.SurahNumber
import com.mfoumby.hassan.quran.domain.VerseNumber

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