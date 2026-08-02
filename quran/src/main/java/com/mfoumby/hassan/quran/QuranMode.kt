package com.mfoumby.hassan.quran

sealed class QuranMode {
    data class SurahMode(val surahNumber: Int): QuranMode()
    data class JuzMode(val juzNumber: Int): QuranMode()
    data class HizbMode(val hizbNumber: Int): QuranMode()
}