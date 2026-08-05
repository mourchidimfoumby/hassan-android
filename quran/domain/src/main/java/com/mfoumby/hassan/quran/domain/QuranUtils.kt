package com.mfoumby.hassan.quran.domain

typealias SurahNumber = Int
typealias JuzNumber = Int
typealias HizbNumber = Int
typealias VerseNumber = Int

object QuranUtils {
    fun calculateHizb(juz: Int) = (juz - 1) / 2 + 1

    fun calculateJuz(hizb: Int) = (hizb - 1) * 2 + 1
}